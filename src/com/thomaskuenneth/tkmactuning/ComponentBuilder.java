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
import com.thomaskuenneth.tkmactuning.plugin.OSAScriptPlugin;
import com.thomaskuenneth.tkmactuning.plugin.StringChooserPlugin;
import java.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * This class is used to create controls (nodes) for plugins.
 *
 * @author Thomas Kuenneth
 */
public class ComponentBuilder {

    public static Node createComponent(AbstractPlugin plugin) {
        Node result = null;
        final String shortDescription = plugin.getShortDescription();
        if (plugin instanceof StringChooserPlugin) {
            String[] possibleValues = ((StringChooserPlugin) plugin).getValues();
            ComboBox combobox = new ComboBox();
            combobox.getItems().addAll(Arrays.asList(possibleValues));
            PluginComponentConnector.connect(plugin, combobox);
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.BASELINE_LEFT);
            hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
            final Label label = new Label(shortDescription);
            label.setLabelFor(combobox);
            hbox.getChildren().add(label);
            hbox.getChildren().add(combobox);
            result = hbox;
        } else if (plugin instanceof BooleanPlugin) {
            result = new CheckBox(shortDescription);
            PluginComponentConnector.connect(plugin, (CheckBox) result);
        } else if (plugin instanceof OSAScriptPlugin) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.TOP_LEFT);
            hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
            final Label label = new Label(shortDescription);
            hbox.getChildren().add(label);
            ImageView imageview = new ImageView();
            StackPane p = new StackPane();
            p.setStyle("-fx-border-insets: 1 1 1 1; -fx-border-color: -fx-text-box-border -fx-text-box-border -fx-text-box-border -fx-text-box-border; -fx-border-width: 1;");
            p.getChildren().add(imageview);
            imageview.setFitWidth(100);
            imageview.setFitHeight(100);
            imageview.setSmooth(true);
            imageview.setPreserveRatio(true);
            label.setLabelFor(imageview);
            hbox.getChildren().add(p);
            result = hbox;
        }
        return result;
    }
}
