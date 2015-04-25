/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskuenneth.tkmactuning;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thomas
 */
public final class Defaults {

    private static final String CMD = "/usr/bin/defaults";

    private Defaults() {
    }

    public static Boolean readBoolean(String domain, String key) {
        String result = read(domain, key);
        if ("1".equals(result)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private static String read(String domain, String key) {
        String result = "";
        StringBuilder sbIS = new StringBuilder();
        StringBuilder sbES = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(CMD, "read", domain, key);
        if (start(pb, sbIS, sbES) == 0) {
            result = sbIS.toString().trim();
        }
        return result;
    }

    public static void write(String domain, String key, Boolean value) {
        write(domain, key, "-bool", value.toString().toUpperCase());
    }

    public static void write(String domain, String key, String value) {
        write(domain, key, "-string", value);
    }

    private static void write(String domain, String key, String type, String value) {
        ProcessBuilder pb = new ProcessBuilder(CMD, "write", domain, key, type, value);
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(Defaults.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Defaults.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                    }
                }
            }
        } catch (IOException e) {
        }
        return exit;
    }
}