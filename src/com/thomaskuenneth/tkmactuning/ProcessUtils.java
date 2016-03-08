/*
 * ProcessUtils.java
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
package com.thomaskuenneth.tkmactuning;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains utility methods to start external commandline tools.
 *
 * @author Thomas Kuenneth
 */
public class ProcessUtils {

    private static final Logger LOGGER = Logger.getLogger(ProcessUtils.class.getName());

    private ProcessUtils() {
    }

    /**
     * Starts a process.
     *
     * @param pb ProcessBuilder instance
     * @param sbIS StringBuilder to recieve the input stream
     * @param sbES StringBuilder to recieve the error stream
     * @return return value of the process
     */
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

    /**
     * Executes /usr/bin/killall"
     *
     * @param applicationName application name
     */
    public static void killall(String applicationName) {
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/killall", applicationName);
        int result = ProcessUtils.start(pb, sbIS, sbES);
        LOGGER.log(Level.INFO, "appName={0} -> {1}", new Object[]{applicationName, result});
    }
}
