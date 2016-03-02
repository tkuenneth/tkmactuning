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

    public static String osascript(String script) {
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/osascript", "-e", script);
        int result = start(pb, sbIS, sbES);
        if (result != 0) {
            LOGGER.log(Level.INFO, sbES.toString());
        }
        return sbIS.toString();
    }

    public static void killall(String applicationName) {
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD_KILLALL, applicationName);
        int result = start(pb, sbIS, sbES);
        LOGGER.log(Level.INFO, "appName={0} -> {1}", new Object[]{applicationName, result});
    }

    public static String read(String domain, String key) {
        String result = "";
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD, "read", domain, key);
        if (start(pb, sbIS, sbES) == 0) {
            result = sbIS.toString().trim();
        }
        LOGGER.log(Level.INFO, "domain={0}, key={1} -> {2}", new Object[]{domain, key, result});
        return result;
    }

    public static void write(String domain, String key, String value) {
        ProcessBuilder pb = new ProcessBuilder(CMD, "write", domain, key, value);
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException ex) {
            LOGGER.log(Level.SEVERE, "exception while writing", ex);
        }
        LOGGER.log(Level.INFO, "domain={0}, key={1}, value={2}", new Object[]{domain, key, value});
    }

    private static int start(ProcessBuilder pb, StringBuilder sbIS, StringBuilder sbES) {
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
