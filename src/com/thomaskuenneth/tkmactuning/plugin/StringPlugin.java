/*
 * StringPlugin.java
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

/**
 * This plugin provides access to string values.
 *
 * @author Thomas Kuenneth
 */
public class StringPlugin extends AbstractPlugin<String> {

    private String value;

    public StringPlugin(String plugin) {
        super(plugin);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public final String getValue() {
        return value;
    }

    @Override
    public final void setValue(String value) {
        this.value = value;
    }

    @Override
    public String convertFromString(String s) {
        // already got a string, so no conversion is needed
        return s;
    }

    @Override
    public String convertToString(String value) {
        return "-String " + value;
    }
}
