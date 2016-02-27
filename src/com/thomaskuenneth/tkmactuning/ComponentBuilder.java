/*
 * ComponentBuilder.java
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
import com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin;
import com.thomaskuenneth.tkmactuning.plugin.Defaults;
import com.thomaskuenneth.tkmactuning.plugin.IFPlugin;
import com.thomaskuenneth.tkmactuning.plugin.StringChooserPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This class is used to create controls for plugins.
 *
 * @author Thomas Kuenneth
 */
public class ComponentBuilder {
    
    private static final List<IFPlugin> L = new ArrayList<>();
    
    public static Node createComponent(AbstractPlugin plugin) {
        Node result = null;
        if (plugin instanceof StringChooserPlugin) {
            String[] possibleValues = ((StringChooserPlugin) plugin).getValues();
            ComboBox combobox = new ComboBox();
            combobox.getItems().addAll(Arrays.asList(possibleValues));
            PluginComponentConnector.connect(plugin, combobox);
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.BASELINE_LEFT);
            hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
            hbox.getChildren().add(new Label(plugin.getShortDescription()));
            hbox.getChildren().add(combobox);
            result = hbox;
        } else if (plugin instanceof BooleanPlugin) {
            result = new CheckBox(plugin.getShortDescription());
            PluginComponentConnector.connect(plugin, (CheckBox) result);
        }
        if (result != null) {
            L.add(plugin);
        }
        return result;
    }
    
    public static void save() {
        HashMap<String, Boolean> map = new HashMap<>();
        L.stream().forEach((plugin) -> {
            String applicationName = plugin.getApplicationName();
            if (!map.containsKey(applicationName)) {
                map.put(applicationName, true);
            }
            plugin.writeValue();
        });
        map.keySet().stream().forEach((applicationName) -> {
            Defaults.killall(applicationName);
        });
    }
}
