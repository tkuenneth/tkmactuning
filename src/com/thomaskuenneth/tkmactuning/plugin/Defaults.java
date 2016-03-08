/*
 * Defaults.java
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

import com.thomaskuenneth.tkmactuning.ProcessUtils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides read and write access to the Mac OS X Defaults database.
 *
 * @author Thomas Kuenneth
 */
public final class Defaults {

    private static final Logger LOGGER = Logger.getLogger(Defaults.class.getName());

    private static final String CMD = "/usr/bin/defaults";

    private Defaults() {
    }

    public static void read(AbstractPlugin plugin) {
        String domain = plugin.getPrimaryCategory();
        String key = plugin.getSecondaryCategory();
        String def = plugin.getString("default");
        if (def != null) {
            setValue(plugin, def);
        }
        String result = null;
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD, "read", domain, key);
        if (ProcessUtils.start(pb, sbIS, sbES) == 0) {
            result = sbIS.toString().trim();
            setValue(plugin, result);
        }
        LOGGER.log(Level.INFO, "domain={0}, key={1} -> {2}", new Object[]{domain, key, result});
    }

    public static void write(AbstractPlugin plugin) {
        String domain = plugin.getPrimaryCategory();
        String key = plugin.getSecondaryCategory();
        String type = null;
        String value = null;
        final Class type1 = plugin.getType();
        if (String.class.equals(type1)) {
            type = "-string";
            value = plugin.getValue().toString();
        } else if (Boolean.class.equals(type1)) {
            type = "-bool";
            value = Boolean.TRUE.equals(plugin.getValue()) ? "TRUE" : "FALSE";
        }
        ProcessBuilder pb = new ProcessBuilder(CMD, "write", domain, key, type, value);
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException ex) {
            LOGGER.log(Level.SEVERE, "exception while writing", ex);
        }
        LOGGER.log(Level.INFO, "domain={0}, key={1}, type={2}, value={3}",
                new Object[]{domain, key, type, value});
    }

    private static void setValue(AbstractPlugin plugin, String data) {
        final Class type = plugin.getType();
        if (String.class.equals(type)) {
            plugin.setValue(data);
        } else if (Boolean.class.equals(type)) {
            plugin.setValue("1".equals(data) || "true".equalsIgnoreCase(data));
        }
    }
}
