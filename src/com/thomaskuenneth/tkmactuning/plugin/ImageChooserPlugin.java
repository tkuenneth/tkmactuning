/*
 * ImageChooserPlugin.java
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
import com.thomaskuenneth.tkmactuning.PluginComponentConnector;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * This plugin provides access to string values. Important: in this plugin the
 * user does not enter strings but picks images stored in the file system. So
 * the string is the file name of the image.
 *
 * @author Thomas Kuenneth
 */
public class ImageChooserPlugin extends StringPlugin {

    private ImageView imageview;

    public ImageChooserPlugin(String plugin) {
        super(plugin);
    }

    @Override
    public Node createNode() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
        final Label label = new Label(getShortDescription());
        hbox.getChildren().add(label);
        imageview = new ImageView();
        imageview.setFitWidth(100);
        imageview.setFitHeight(100);
        imageview.setSmooth(true);
        imageview.setPreserveRatio(true);
        label.setLabelFor(imageview);
        hbox.getChildren().add(imageview);
        PluginComponentConnector.register(this, imageview);
        return hbox;
    }

    @Override
    public void updateNode() {
        String value = (String) getValue();
        if (value != null) {
            Image i = new Image(new File(value).toURI().toString(), 100, 100, true, true);
            imageview.setImage(i);
        }
    }
}
