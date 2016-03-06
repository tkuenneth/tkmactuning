/*
 * DirChooserPlugin.java
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

import com.thomaskuenneth.tkmactuning.JavaFXUtils;
import com.thomaskuenneth.tkmactuning.TKMacTuning;
import java.io.File;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/**
 * This plugin provides access to string values. The string is interpreted as a
 * path name. The plugin can be configured to append the file separator string
 * by setting the configuration key named separator to true.
 *
 * @author Thomas Kuenneth
 */
public class DirChooserPlugin extends StringPlugin {
    
    public DirChooserPlugin(String plugin) {
        super(plugin);
    }
    
    @Override
    public void setValue(String value) {
        String separator = getString("separator");
        if ((separator != null) && ("true".equalsIgnoreCase(separator))) {
            if (!value.endsWith(File.separator)) {
                value += File.separator;
            }
        }
        super.setValue(value);
    }

    @Override
    public Node createNode() {
        node = super.createNode();
        Button button = new Button(TKMacTuning.getString("browse"));
        button.setOnAction(event -> {
            DirectoryChooser ch = new DirectoryChooser();
            String value = getValue();
            if (value != null) {
                File dir = new File(value);
                ch.setInitialDirectory(dir);
            }
            Window w = JavaFXUtils.getWindow(event.getSource());
            File f = ch.showDialog(w);
            if (f != null) {
                String newValue = f.getAbsolutePath();
                setValue(newValue);
                updateNode();
            }
        });
        if (node instanceof HBox) {
            HBox hbox = (HBox) node;
            hbox.getChildren().add(button);
        }
        return node;
    }
}
