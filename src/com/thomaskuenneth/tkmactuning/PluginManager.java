/*
 * PluginManager.java
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
package com.thomaskuenneth.tkmactuning;

import com.thomaskuenneth.tkmactuning.plugin.AbstractPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages plugins.
 *
 * @author Thomas Kuenneth
 */
public class PluginManager {

    private static final List<AbstractPlugin> L = new ArrayList<>();

    public static void register(AbstractPlugin plugin) {
        L.add(plugin);
    }

    /**
     * If the current plugin value differs from the last read value, save the
     * new value and kill the corresponding app.
     */
    public static void save() {
        HashMap<String, Boolean> map = new HashMap<>();
        final AtomicCounter count = new AtomicCounter();
        final Runnable done = () -> {
            count.decrement();
        };
        L.stream().forEach((plugin) -> {
            final Object lastReadOrWritten = plugin.getLastReadOrWritten();
            if ((lastReadOrWritten == null) || (!lastReadOrWritten.equals(plugin.getValue()))) {
                String applicationName = plugin.getApplicationName();
                if ((!map.containsKey(applicationName)) && plugin.isNeedsKillAll()) {
                    map.put(applicationName, true);
                }
                count.increment();
                plugin.writeValue(done);
            }
        });
        Thread t = new Thread(() -> {
            while (count.value() > 0) {
            }
            map.keySet().stream().forEach((String applicationName) -> {
                ProcessUtils.killall(applicationName);
            });
        });
        t.start();
    }

    /**
     * Rereads the values and updates the ui.
     */
    public static void reread() {
        L.stream().forEach((plugin) -> {
            plugin.readValue(null);
        });
    }
}
