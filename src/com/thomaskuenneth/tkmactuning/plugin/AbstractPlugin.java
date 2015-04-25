/*
 * AbstractPlugin.java
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 * This abstract class provides access to values stored in the Mac OS X
 * Defaults database. The are defined by a domain and a key.<p>
 * The class has builtin support for bound properties through
 * <code>addPropertyChangeListener()</code> and 
 * <code>removePropertyChangeListener()</code>.
 * Subclasses must invoke <code>firePropertyChange()</code> of
 * the <code>pcs</code> instance variable.
 * 
 * @author Thomas Kuenneth
 */
public abstract class AbstractPlugin<T> implements IFPlugin<T> {

    private ResourceMap resourceMap;
    protected PropertyChangeSupport pcs;

    protected AbstractPlugin() {
        this(AbstractPlugin.class);
    }

    protected AbstractPlugin(Class clazz) {
        resourceMap = Application.getInstance().getContext().getResourceMap(clazz);
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propName,
            PropertyChangeListener l) {
        pcs.addPropertyChangeListener(propName, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propName,
            PropertyChangeListener l) {
        pcs.removePropertyChangeListener(propName, l);
    }
    
    public final String getString(String key) {
        return resourceMap.getString(key);
    }

    protected final String getDomain() {
        return getString("domain");
    }

    protected final String getKey() {
        return getString("key");
    }

    public final String getShortDescription() {
        return getString("shortDescription");
    }

    public final String getLongDescription() {
        return getString("longDescription");
    }

    public final String getApplicationName() {
        return getString("applicationName");
    }

    public final String getPrimaryCategory() {
        return getString("primaryCategory");
    }

    public final String getSecondaryCategory() {
        return getString("secondaryCategory");
    }
}
