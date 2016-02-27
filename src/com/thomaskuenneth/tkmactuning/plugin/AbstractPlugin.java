/*
 * AbstractPlugin.java
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

import com.thomaskuenneth.tkmactuning.TKMacTuning;

/**
 * This is an abstract base class for plugins.
 *
 * @author Thomas Kuenneth
 * @param <T> the type of the value being handled by the plugin
 */
public abstract class AbstractPlugin<T> implements IFPlugin<T> {

    private final String pluginName;

    public AbstractPlugin(String pluginName) {
        this.pluginName = pluginName;
        readValue();
    }

    @Override
    public final String getShortDescription() {
        return getString("shortDescription");
    }

    @Override
    public final String getLongDescription() {
        return getString("longDescription");
    }

    @Override
    public final String getApplicationName() {
        return getString("applicationName");
    }

    @Override
    public final String getPrimaryCategory() {
        return getString("primaryCategory");
    }

    @Override
    public final String getSecondaryCategory() {
        return getString("secondaryCategory");
    }

    @Override
    public String getUICategory() {
        String uiCategory = getString("uiCategory");
        if (uiCategory == null) {
            uiCategory = getApplicationName();
        }
        return uiCategory;
    }

    final String getString(String key) {
        String result = TKMacTuning.getString(pluginName + "." + key);
        if ((result != null) && (result.startsWith("$$$"))) {
            result = TKMacTuning.getString(result);
        }
        return result;
    }
}
