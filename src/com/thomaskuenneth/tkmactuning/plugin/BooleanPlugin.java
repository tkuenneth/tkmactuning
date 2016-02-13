/*
 * BooleanPlugin.java
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
 * This plugin provides access to boolean values in the Mac OS X Defaults
 * database.
 *
 * @author Thomas Kuenneth
 */
public class BooleanPlugin extends AbstractPlugin<Boolean> {

    private Boolean value = Boolean.FALSE;

    public BooleanPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public final Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public final Boolean getValue() {
        return value;
    }

    @Override
    public final void setValue(Boolean value) {
        Boolean old = this.value;
        this.value = value;
        pcs.firePropertyChange("value", old, value);
    }

    @Override
    public final void readValue() {
        setValue(Defaults.readBoolean(getPrimaryCategory(), getSecondaryCategory()));
    }

    @Override
    public final void writeValue() {
        Defaults.write(getPrimaryCategory(), getSecondaryCategory(), getValue());
    }
}
