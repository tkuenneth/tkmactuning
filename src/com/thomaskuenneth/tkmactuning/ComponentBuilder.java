/*
 * ComponentBuilder.java
 *
 * Copyright 2008 Thomas Kuenneth
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
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * This class is used to create components for plugins.
 *
 * @author Thomas Kuenneth
 */
public class ComponentBuilder {

    public static JComponent createComponent(AbstractPlugin plugin) {
        JComponent result = null;
        Class type = plugin.getType();
        if (Boolean.class.equals(type)) {
            result = new JCheckBox(plugin.getShortDescription());
            PluginComponentConnector.connect(plugin, result);
        }
        if (result != null) {
            result.putClientProperty("plugin", plugin);
        }
        return result;
    }
}
