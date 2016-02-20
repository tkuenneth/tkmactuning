/*
 * PluginComponentConnector.java
 *
 * Copyright 2008 - 2016 Thomas Kuenneth
 *
 * This file is part of TKMacTuning.
 *
 * TKMacTuning is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation
 *
 * TKMacTuning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TKMacTuning; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.thomaskuenneth.tkmactuning;

import com.thomaskuenneth.tkmactuning.plugin.AbstractPlugin;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;

/**
 * This class connects plugins to controls.
 *
 * @author Thomas Kuenneth
 */
public class PluginComponentConnector {

    public static void connect(final AbstractPlugin plugin, Control control) {
        if (control instanceof CheckBox) {
            final CheckBox cb = ((CheckBox) control);
            cb.setSelected((boolean) plugin.getValue());
            cb.setOnAction((event) -> {
                plugin.setValue(cb.isSelected());
            });
        }
    }
}
