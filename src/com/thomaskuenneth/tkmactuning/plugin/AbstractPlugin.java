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

import com.thomaskuenneth.tkmactuning.TKMacTuning;

/**
 * This is the abstract base class for TKMacTuning-plugins. Plugins provide read
 * and write access to a value of a given type. <code>getValue()</code> and
 * <code>setValue()</code> always work on local copies of the value, which are
 * obtained from an external source by invoking <code>readValue()</code> and can
 * be stored externally with <code>writeValue()</code>. The external source is
 * managed by a value provider. <br>
 * Currently the value providers are hardcoded; this might change if needed.
 *
 * @author Thomas Kuenneth
 * @param <T> the type of the value being handled by the plugin
 */
public abstract class AbstractPlugin<T> {

    public static final String ROOT = "root";

    private static final String VALUEPROVIDER_DEFAULTS = "com.thomaskuenneth.tkmactuning.plugin.DefaultsValueProvider";
    private static final String VALUEPROVIDER_OSASCRIPT = "com.thomaskuenneth.tkmactuning.plugin.OSAScriptValueProvider";

    private final String pluginName;
    private final String valueProvider;

    public AbstractPlugin(String pluginName) {
        this.pluginName = pluginName;
        valueProvider = getString("valueprovider");
        if (valueProvider == null) {
            throw new RuntimeException("valueprovider not set");
        }
        readValue();
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

    public final void readValue() {
        if (null != valueProvider) {
            switch (valueProvider) {
                case VALUEPROVIDER_DEFAULTS:
                    Defaults.read(this);
                    break;
                case VALUEPROVIDER_OSASCRIPT:
//                    String cmd = String.format("tell application \"%s\" to %s",
//                            getString("applicationName"),
//                            getString("read"));
//                    String result = Defaults.osascript(cmd).trim();
                    //setValue(convertFromString(result));
                    break;
            }
        }
    }

    public final void writeValue() {
        if (null != valueProvider) {
            switch (valueProvider) {
                case VALUEPROVIDER_DEFAULTS:
                    Defaults.write(this);
                    break;
                case VALUEPROVIDER_OSASCRIPT:
                    break;
            }
        }
    }

    final String getString(String key) {
        String result = TKMacTuning.getString(pluginName + "." + key);
        if ((result != null) && (result.startsWith("$$$"))) {
            result = TKMacTuning.getString(result);
        }
        return result;
    }
}
