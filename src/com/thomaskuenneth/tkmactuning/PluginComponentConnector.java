/*
 * PluginComponentConnector.java
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
package com.thomaskuenneth.tkmactuning;

import com.thomaskuenneth.tkmactuning.plugin.AbstractPlugin;
import com.thomaskuenneth.tkmactuning.plugin.Defaults;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class connects plugins to controls.
 *
 * @author Thomas Kuenneth
 */
public class PluginComponentConnector {

    private static final Map<AbstractPlugin, Node> M = new HashMap();

    /**
     * Connect a plugin to a checkbox.
     *
     * @param plugin plugin
     * @param checkbox checkbox
     */
    public static void connect(final AbstractPlugin plugin, CheckBox checkbox) {
        updateCheckBox(plugin, checkbox);
        checkbox.setOnAction((event) -> {
            plugin.setValue(checkbox.isSelected());
        });
        M.put(plugin, checkbox);
    }

    /**
     * Connect a plugin to a combobox.
     *
     * @param plugin plugin
     * @param combobox combobox
     */
    public static void connect(final AbstractPlugin plugin, ComboBox combobox) {
        updateComboBox(plugin, combobox);
        combobox.setOnAction(event -> {
            plugin.setValue(combobox.getValue());
        });
        M.put(plugin, combobox);
    }

    public static void connect(final AbstractPlugin plugin, ImageView imageview) {
        updateImageView(plugin, imageview);
        // FIXME: react upon actions
        M.put(plugin, imageview);
    }

    /**
     * Saves the current plugin state and kills the corresponding apps.
     */
    public static void save() {
        // TODO: check if the state has changed; if not, app does not need to be killed
        HashMap<String, Boolean> map = new HashMap<>();
        M.keySet().stream().forEach((plugin) -> {
            String applicationName = plugin.getApplicationName();
            if (!map.containsKey(applicationName)) {
                map.put(applicationName, true);
            }
            plugin.writeValue();
        });
        map.keySet().stream().forEach((String applicationName) -> {
            Defaults.killall(applicationName);
        });
    }

    /**
     * Resets the ui to the last saved values.
     */
    public static void reset() {
        M.keySet().stream().forEach((plugin) -> {
            plugin.readValue();
            Node node = M.get(plugin);
            if (node instanceof CheckBox) {
                updateCheckBox(plugin, (CheckBox) node);
            } else if (node instanceof ComboBox) {
                updateComboBox(plugin, (ComboBox) node);
            } else if (node instanceof ImageView) {
                updateImageView(plugin, (ImageView) node);
            }
        });
    }

    private static void updateCheckBox(final AbstractPlugin plugin, CheckBox checkbox) {
        checkbox.setSelected((boolean) plugin.getValue());
    }

    private static void updateComboBox(final AbstractPlugin plugin, ComboBox combobox) {
        combobox.setValue(plugin.getValue());
    }

    private static void updateImageView(final AbstractPlugin plugin, ImageView imageview) {
        String value = (String) plugin.getValue();
        Image i = new Image(new File(value).toURI().toString(), 100, 100, true, true);
        imageview.setImage(i);
    }
}
