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

import java.io.IOException;
import java.io.InputStream;
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
    private static final String CMD_KILLALL = "/usr/bin/killall";

    private Defaults() {
    }

    public static void killall(String applicationName) {
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD_KILLALL, applicationName);
        int result = start(pb, sbIS, sbES);
        LOGGER.log(Level.INFO, "appName={0} -> {1}", new Object[]{applicationName, result});
    }

    public static void read(AbstractPlugin plugin) {
        String domain = plugin.getPrimaryCategory();
        String key = plugin.getSecondaryCategory();
        String result = null;
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD, "read", domain, key);
        if (start(pb, sbIS, sbES) == 0) {
            result = sbIS.toString().trim();
            final Class type = plugin.getType();
            if (String.class.equals(type)) {
                plugin.setValue(result);
            } else if (Boolean.class.equals(type)) {
                plugin.setValue("1".equals(result) || "true".equalsIgnoreCase(result));
            }
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

    public static int start(ProcessBuilder pb, StringBuilder sbIS, StringBuilder sbES) {
        int exit = 1;
        try {
            Process p = pb.start();
            InputStream is = p.getInputStream();
            int isData;
            InputStream es = p.getErrorStream();
            int esData;
            while (true) {
                isData = is.read();
                esData = es.read();
                if (isData != -1) {
                    sbIS.append(new Character((char) isData));
                }
                if (esData != -1) {
                    sbES.append(new Character((char) esData));
                }
                if ((isData == -1) && (esData == -1)) {
                    try {
                        exit = p.exitValue();
                        break;
                    } catch (IllegalThreadStateException e) {
                        // no logging needed... just waiting
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "exception while reading", e);
        }
        return exit;
    }
}
