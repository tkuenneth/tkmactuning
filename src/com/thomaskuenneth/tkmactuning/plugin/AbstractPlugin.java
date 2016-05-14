/*
 * AbstractPlugin.java
 *
 * Copyright 2008 - 2016 Thomas Kuenneth
 *
 * This file is part of TKMacTuning.
 *
 * TKMacTuning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.thomaskuenneth.tkmactuning.plugin;

import com.thomaskuenneth.tkmactuning.PluginManager;
import com.thomaskuenneth.tkmactuning.ProcessUtils;
import com.thomaskuenneth.tkmactuning.TKMacTuning;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

/**
 * This is the abstract base class for TKMacTuning-plugins. Plugins provide
 * read/write access to a value of a given type. <code>getValue()</code> and
 * <code>setValue()</code> work on local copies of the value. The local copy is
 * obtained from an external source by invoking <code>readValue()</code>. It is
 * stored externally by invoking <code>writeValue()</code>. The external source
 * is managed by a value provider. Currently the value providers are hardcoded.
 * <br>Which data is handled by the plugin is defined by the <em>plugin
 * name</em>. It is used as a key to a configuration which currently resides in
 * TKMacTuning.properties.
 *
 * @author Thomas Kuenneth
 * @param <T> the type of the value being handled by the plugin
 */
public abstract class AbstractPlugin<T> {

    public static final String ROOT = "root";

    private static final String VALUEPROVIDER_DEFAULTS = "com.thomaskuenneth.tkmactuning.plugin.DefaultsValueProvider";
    private static final String VALUEPROVIDER_OSASCRIPT = "com.thomaskuenneth.tkmactuning.plugin.OSAScriptValueProvider";

    protected final TKMacTuning app;
    
    private final String pluginName;
    private final String valueProvider;

    protected Node node;
    
    private T lastReadOrWritten;

    public AbstractPlugin(TKMacTuning app, String pluginName) {
        this.app = app;
        this.pluginName = pluginName;
        valueProvider = getString("valueprovider");
        if (valueProvider == null) {
            throw new RuntimeException("valueprovider not set");
        }
        initialize();
    }

    private void initialize() {
        node = createNode();
        String tooltipText = getString("tooltip");
        if (tooltipText != null) {
            Tooltip t = new Tooltip(tooltipText);
            Tooltip.install(node, t);
        }
        readValue(null);
        PluginManager.register(this);
    }

    public final String getShortDescription() {
        return getString("shortDescription");
    }

    public final String getLongDescription() {
        return getString("longDescription");
    }

    public final String getApplicationName() {
        return getString("applicationName");
    }

    public final String getPrimaryCategory() {
        return getString("primaryCategory");
    }

    public final String getSecondaryCategory() {
        return getString("secondaryCategory");
    }

    public final String getPrimaryUICategory() {
        String primaryUICategory = getString("primaryUICategory");
        if (primaryUICategory == null) {
            primaryUICategory = getApplicationName();
        }
        return primaryUICategory;
    }

    public final String getSecondaryUICategory() {
        String secondaryUICategory = getString("secondaryUICategory");
        if (secondaryUICategory == null) {
            secondaryUICategory = ROOT;
        }
        return secondaryUICategory;
    }

    public abstract Class<T> getType();

    public abstract T getValue();

    public abstract void setValue(T value);

    /**
     * Returns the value that was last read from or written to the value
     * provider.
     *
     * @return value that was last read from or written to the value provider
     */
    public final T getLastReadOrWritten() {
        return lastReadOrWritten;
    }

    /**
     * Reads the value from an external source using the configured value
     * provider. Afterwards the ui is updated by calling
     * <code>updateNode()</code>.
     *
     * @param done invoked on the JavaFX Application Thread after the value has
     * been read; may be null
     */
    public final void readValue(Runnable done) {
        Task<Void> t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (null != valueProvider) {
                    switch (valueProvider) {
                        case VALUEPROVIDER_DEFAULTS:
                            Defaults.read(AbstractPlugin.this);
                            break;
                        case VALUEPROVIDER_OSASCRIPT:
                            OSAScript.read(AbstractPlugin.this);
                            break;
                    }
                }
                lastReadOrWritten = AbstractPlugin.this.getValue();
                Platform.runLater(() -> {
                    updateNode();
                });
                if (done != null) {
                    Platform.runLater(done);
                }
                return null;
            }
        };
        Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Writes the value to an external source using the configured value
     * provider. If the plugin is an action, asassa
     *
     * @param done invoked after the value has been written; may be null
     */
    public final void writeValue(Runnable done) {
        preWriteValue();
        Task<Void> t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (null != valueProvider) {
                    switch (valueProvider) {
                        case VALUEPROVIDER_DEFAULTS:
                            Defaults.write(AbstractPlugin.this);
                            break;
                        case VALUEPROVIDER_OSASCRIPT:
                            OSAScript.write(AbstractPlugin.this);
                            break;
                    }
                    if (isAction()) {
                        ProcessUtils.killall(getApplicationName());
                    }
                }
                lastReadOrWritten = AbstractPlugin.this.getValue();
                if (done != null) {
                    Platform.runLater(done);
                }
                return null;
            }
        };
        Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();
    }
    
    /**
     * Can be overridden if something needs to be done before writing the value
     */
    public void preWriteValue() {
        // intentionally does nothing
    }

    /**
     * Returns the ui that represents the plugin.
     *
     * @return ui that represents the plugin
     */
    public final Node getNode() {
        return node;
    }

    /**
     * Creates the ui that represents the plugin.
     *
     * @return ui that represents the plugin
     */
    public abstract Node createNode();

    /**
     * This method is called when the plugin should update its ui.
     */
    public abstract void updateNode();

    /**
     * Returns the property needsKillAll. If the property is not set, true is
     * returned (it is then assumed that a plugin needs to have killall
     * invoked). If the plugin is an action plugin, false is returned.
     *
     * @return the property needsKillAll
     */
    public final boolean isNeedsKillAll() {
        String s = getString("needsKillAll");
        return !isAction() && !((s != null) && "false".equalsIgnoreCase(s));
    }
    
    /**
     * Returns the property isAction. If the property is not set, false is
     * returned (it is then assumed that a plugin is not an action). If a plugin
     * is an action, readValue() is never called, and writeValue() is called
     * upon activation of the action.
     *
     * @return the property isAction
     */
    public final boolean isAction() {
        String s = getString("isAction");
        return ((s != null) && "true".equalsIgnoreCase(s));
    }

    final String getString(String key) {
        String result = app.getString(pluginName + "." + key);
        if ((result != null) && (result.startsWith("$$$"))) {
            result = app.getString(result);
        }
        return result;
    }
}
