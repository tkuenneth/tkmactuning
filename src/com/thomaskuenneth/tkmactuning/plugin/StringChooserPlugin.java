/*
 * StringChooserPlugin.java
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

/**
 * This plugin provides access to string values. Important: in this plugin the
 * user does not enter strings but picks one from a predefined list. List items
 * are separated with the pipe symbol.
 *
 * @author Thomas Kuenneth
 */
public class StringChooserPlugin extends StringPlugin {

    public StringChooserPlugin(String plugin) {
        super(plugin);
    }

    /**
     * Returns a list of predefined strings
     *
     * @return a list of predefined strings
     */
    public String[] getValues() {
        String values = getString("values");
        if (values != null) {
            String[] possibleValues = values.split("\\|");
            for (int i = 0; i < possibleValues.length; i++) {
                possibleValues[i] = possibleValues[i].trim();
            }
            return possibleValues;
        }
        return null;
    }
}
