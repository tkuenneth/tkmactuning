/*
 * ComponentBuilder.java
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
import com.thomaskuenneth.tkmactuning.plugin.IFPlugin;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is used to create components for plugins.
 *
 * @author Thomas Kuenneth
 */
public class ComponentBuilder {

    private static final List<IFPlugin> L = new ArrayList<>();

    public static JComponent createComponent(AbstractPlugin plugin) {
        Class type = plugin.getType();
        if (Boolean.class.equals(type)) {
            JCheckBox cb = new JCheckBox(plugin.getShortDescription());
            configure(plugin, cb);
            return cb;
        } else if (String.class.equals(type)) {
            JTextField tf = new JTextField(40);
            configure(plugin, tf);
            JPanel p = new JPanel();
            p.add(new JLabel(plugin.getShortDescription()));
            p.add(tf);
            return p;
        }
        return null;
    }

    private static void configure(AbstractPlugin plugin, JComponent c) {
        PluginComponentConnector.connect(plugin, c);
        L.add(plugin);
    }

    public static void save() {
        for (IFPlugin plugin : L) {
            plugin.writeValue();
        }
    }
}
