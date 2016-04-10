/*
 * ActionPlugin.java
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

import com.thomaskuenneth.tkmactuning.TKMacTuning;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * This plugin executes actions.
 *
 * @author Thomas Kuenneth
 */
public class ActionPlugin extends AbstractPlugin<String> {

    public ActionPlugin(TKMacTuning app, String pluginName) {
        super(app, pluginName);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String getValue() {
        return getString("commandData");
    }

    @Override
    public void setValue(String value) {
        // there is no value
    }

    @Override
    public Node createNode() {
        Button button = new Button(getShortDescription());
        button.setOnAction(event -> {
            writeValue(() -> {
                app.getStatusBar().setMainText(String.format(app.getString("msg_write"),
                        getShortDescription()));
            });
        });
        return button;
    }

    @Override
    public void updateNode() {
        // no need to update as there is no value that might have changed
    }

}
