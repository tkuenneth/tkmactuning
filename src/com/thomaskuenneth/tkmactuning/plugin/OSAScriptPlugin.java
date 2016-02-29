/*
 * OSAScriptPlugin.java
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
 * This plugin provides access to string values via osascript.
 *
 * @author Thomas Kuenneth
 */
public class OSAScriptPlugin extends StringPlugin {

    public OSAScriptPlugin(String plugin) {
        super(plugin);
    }

    @Override
    public void readValue() {
        String cmd = String.format("tell application \"%s\" to %s",
                getString("applicationName"),
                getString("read"));
        String result = Defaults.osascript(cmd).trim();
        setValue(result);
    }

    @Override
    public void writeValue() {
    }
}
