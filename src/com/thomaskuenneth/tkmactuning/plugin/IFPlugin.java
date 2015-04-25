/*
 * IFPlugin.java
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

/**
 * This interface defines the methods a TKMacTuning-plugin exposes.
 * Plugins provide read and write access to a value of some type.
 * <code>getValue()</code> and <code>setValue()</code> always work on
 * local copies of the value, which are obtained from an
 * external source by invoking <code>readValue()</code> and can be
 * stored externally with <code>writeValue()</code>.
 *
 * @author Thomas Kuenneth
 */
public interface IFPlugin<T> {

    /**
     * Returns the type of the value a plugin handles.
     * 
     * @return the type
     */
    public Class<T> getType();

    /**
     * Returns a local copy of the value.
     * 
     * @return the value
     */
    public T getValue();

    /**
     * Sets the local copy of the value.
     * 
     * @param value the new value
     */
    public void setValue(T value);

    /**
     * Reads the value from an external source and sets the local copy.
     */
    public void readValue();

    /**
     * Saves the local copy of the value in an external location.
     */
    public void writeValue();

    /**
     * Returns a short description of the plugin.
     * 
     * @return the short description
     */
    public String getShortDescription();

    /**
     * Returns a long description of the plugin.
     * 
     * @return the long description
     */
    public String getLongDescription();

    /**
     * Returns the human readable name of the application
     * the value belongs to
     * 
     * @return the name of the application
     */
    public String getApplicationName();

    /**
     * Returns the name of the primary category this value
     * belongs to
     * 
     * @return the primary category
     */
    public String getPrimaryCategory();

    /**
     * Returns the name of the secondary category this value
     * belongs to
     * 
     * @return the secondary category
     */
    public String getSecondaryCategory();
}
