/*
 * BooleanPlugin.java
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

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

/**
 * This plugin provides access to boolean values.
 *
 * @author Thomas Kuenneth
 */
public class BooleanPlugin extends AbstractPlugin<Boolean> {

    private Boolean value;
    private CheckBox checkbox;

    public BooleanPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public final Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public final Boolean getValue() {
        return value;
    }

    @Override
    public final void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Node createNode() {
        checkbox = new CheckBox(getShortDescription());
        checkbox.setOnAction((event) -> {
            setValue(checkbox.isSelected());
        });
        return checkbox;
    }

    @Override
    public void updateNode() {
        if (getValue() != null) {
            checkbox.setSelected(getValue());
        }
    }
}
