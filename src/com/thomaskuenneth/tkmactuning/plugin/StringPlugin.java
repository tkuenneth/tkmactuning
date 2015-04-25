/*
 * StringPlugin.java
 *
 * Copyright 2008 Thomas Kuenneth
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

import com.thomaskuenneth.tkmactuning.Defaults;

/**
 * This plugin provides access to string values in the Mac OS X
 * Defaults database.
 *
 * @author Thomas Kuenneth
 */
public abstract class StringPlugin extends AbstractPlugin<String> {

    private String value = null;

    public StringPlugin(Class clazz) {
        super(clazz);
        readValue();
    }

    public final Class<String> getType() {
        return String.class;
    }

    public final String getValue() {
        return value;
    }

    public final void setValue(String value) {
        String old = this.value;
        this.value = value;
        System.err.println(getShortDescription() + ": " + value);
        pcs.firePropertyChange("value", old, value);
    }

    public final void readValue() {
//        setValue(Defaults.readBoolean(getDomain(), getKey()));
    }

    public final void writeValue() {
        Defaults.write(getDomain(), getKey(), getValue());
    }
}
