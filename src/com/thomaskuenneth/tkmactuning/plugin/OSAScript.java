/*
 * OSAScript.java
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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides read and write access to data using the osascript
 * command.
 *
 * @author Thomas Kuenneth
 */
public class OSAScript {

    private static final Logger LOGGER = Logger.getLogger(OSAScript.class.getName());

    /**
     * Obtains data by running a script using the osascript command.
     *
     * @param plugin the plugin
     */
    public static void read(AbstractPlugin plugin) {
        String script = String.format("tell application \"%s\" to %s",
                plugin.getApplicationName(),
                plugin.getString("read"));
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/osascript", "-e", script);
        int result = Defaults.start(pb, sbIS, sbES);
        if (result == 0) {
            String value = sbIS.toString().trim();
            final Class type = plugin.getType();
            if (String.class.equals(type)) {
                plugin.setValue(value);
            } else if (Boolean.class.equals(type)) {
                throw new RuntimeException("not implemented yet");
            }
        } else {
            // FIXME: react to default valze if it was set
            LOGGER.log(Level.SEVERE, sbES.toString());
        }
    }
}
