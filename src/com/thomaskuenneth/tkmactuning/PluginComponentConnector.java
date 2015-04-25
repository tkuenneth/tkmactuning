/*
 * PluginComponentConnector.java
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
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;

/**
 * This class connects plugins to components. It binds the Swing components to
 * the <em>value</em> property of <code>AbstractPlugin</code> and its
 * children.
 *
 * @author Thomas Kuenneth
 */
public class PluginComponentConnector {

    public static void connect(AbstractPlugin plugin, JComponent component) {
        Property value = BeanProperty.create("value");
        if (component instanceof JCheckBox) {
            Property selected = BeanProperty.create("selected");
            Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, plugin, value, component, selected);
            b.bind();
        }
    }
}
