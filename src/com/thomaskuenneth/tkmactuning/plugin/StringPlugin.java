/*
 * StringPlugin.java
 *
 * Copyright 2008 - 2016 Thomas Kuenneth
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
package com.thomaskuenneth.tkmactuning.plugin;

/**
 * This plugin provides access to string values in the Mac OS X Defaults
 * database.
 *
 * @author Thomas Kuenneth
 */
public class StringPlugin extends AbstractPlugin<String> {

    private String value = "";

    public StringPlugin(String plugin) {
        super(plugin);
    }

    @Override
    public final Class<String> getType() {
        return String.class;
    }

    @Override
    public final String getValue() {
        return value;
    }

    @Override
    public final void setValue(String value) {
        String old = this.value;
        this.value = value;
        pcs.firePropertyChange("value", old, value);
    }

    @Override
    public final void readValue() {
        setValue(Defaults.readString(getPrimaryCategory(), getSecondaryCategory()));
    }

    @Override
    public final void writeValue() {
        Defaults.write(getPrimaryCategory(), getSecondaryCategory(), getValue());
    }
}
