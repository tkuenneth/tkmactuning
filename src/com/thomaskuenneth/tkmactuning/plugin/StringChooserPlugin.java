/*
 * StringChooserPlugin.java
 *
 * Copyright 2016 Thomas Kuenneth
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

import com.thomaskuenneth.tkmactuning.LayoutConstants;
import com.thomaskuenneth.tkmactuning.TKMacTuning;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This plugin provides access to string values. Important: in this plugin the
 * user does not enter strings but picks one from a predefined list. List items
 * are separated with the pipe symbol.
 *
 * @author Thomas Kuenneth
 */
public class StringChooserPlugin extends StringPlugin {

    private Map<String, String> kvp;
    private ComboBox combobox;

    public StringChooserPlugin(TKMacTuning app, String plugin) {
        super(app, plugin);
    }

    /**
     * Returns a list of predefined strings
     *
     * @return a list of predefined strings
     */
    public final String[] getValues() {
        String values = getString("values");
        if (values != null) {
            String[] possibleValues = values.split("\\|");
            int i = 0;
            for (String key : possibleValues) {
                key = key.trim();
                int pos;
                if (((pos = key.indexOf("=")) > 0) && ((pos + 1) < key.length())) {
                    String value = key.substring(pos + 1);
                    key = key.substring(0, pos);
                    kvp.put(key, value);
                } else {
                    kvp.put(key, key);
                }
                possibleValues[i++] = key;
            }
            return possibleValues;
        }
        return new String[]{};
    }

    @Override
    public Node createNode() {
        kvp =  new HashMap<>();
        String[] possibleValues = getValues();
        combobox = new ComboBox();
        combobox.getItems().addAll(Arrays.asList(possibleValues));
        combobox.setOnAction(event -> {
            setValue((String) kvp.get((String) combobox.getValue()));
        });
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.BASELINE_LEFT);
        hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
        final Label label = new Label(getShortDescription());
        label.setLabelFor(combobox);
        hbox.getChildren().add(label);
        hbox.getChildren().add(combobox);
        return hbox;
    }

    @Override
    public void updateNode() {
        String value = getValue();
        kvp.keySet().forEach(v -> {
            if (kvp.get(v).equals(value)) {
                combobox.setValue(v);
            }
        });
    }
}
