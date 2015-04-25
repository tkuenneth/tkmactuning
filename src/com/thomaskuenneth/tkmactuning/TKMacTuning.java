/*
 * TKMacTuning.java
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

import com.thomaskuenneth.tkmactuning.plugin.FXListViewStripes;
import com.thomaskuenneth.tkmactuning.plugin.IFPlugin;
import com.thomaskuenneth.tkmactuning.plugin.IncludeDebugMenu;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.Box;
import javax.swing.JComponent;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * This is the main class of TKMacTuning. It acts as the entry point
 * of the program. The gui is created and shown here.
 *
 * @author Thomas Kuenneth
 */
public class TKMacTuning extends SingleFrameApplication implements Application.ExitListener {

    private Box b;

    @Override
    protected void startup() {
        addExitListener(this);
        b = Box.createVerticalBox();
        b.add(ComponentBuilder.createComponent(new FXListViewStripes()));
        b.add(ComponentBuilder.createComponent(new IncludeDebugMenu()));
        show(b);
    }

    /**
     * Initiates the startup sequence.
     * 
     * @param args arguments from the command line
     */
    public static void main(String[] args) {
        Application.launch(TKMacTuning.class, args);
    }

    public boolean canExit(EventObject arg0) {
        return true;
    }

    public void willExit(EventObject arg0) {
        for (Component c : b.getComponents()) {
            if (c instanceof JComponent) {
                JComponent cc = (JComponent) c;
                IFPlugin p = (IFPlugin) cc.getClientProperty("plugin");
                if (p != null) {
                    p.writeValue();
                }
            }
        }

    }
}
