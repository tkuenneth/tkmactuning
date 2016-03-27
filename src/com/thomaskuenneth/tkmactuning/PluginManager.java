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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * This class manages plugins.
 *
 * @author Thomas Kuenneth
 */
public class PluginManager {

    private static final Logger LOGGER = Logger.getLogger(PluginManager.class.getName());
    
    private static final List<AbstractPlugin> L = new ArrayList<>();

    public static void register(AbstractPlugin plugin) {
        L.add(plugin);
    }

    /**
     * If the current plugin value differs from the last read value, save the
     * new value and kill the corresponding app.
     *
     * @param app app instance
     */
    public static void save(TKMacTuning app) {
        HashMap<String, Boolean> map = new HashMap<>();
        final AtomicCounter count = new AtomicCounter();
        L.stream().forEach((plugin) -> {
            final Object lastReadOrWritten = plugin.getLastReadOrWritten();
            if ((lastReadOrWritten == null) || (!lastReadOrWritten.equals(plugin.getValue()))) {
                String applicationName = plugin.getApplicationName();
                if ((!map.containsKey(applicationName)) && plugin.isNeedsKillAll()) {
                    map.put(applicationName, true);
                }
                count.increment();
                plugin.writeValue(() -> {
                    app.getStatusBar().setMainText(String.format(app.getString("msg_write"),
                            plugin.getShortDescription()));
                    count.decrement();
                });
            }
        });
        if (map.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.initStyle(StageStyle.UTILITY);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.getButtonTypes().add(ButtonType.NO);
            Window owner = JavaFXUtils.getWindow(app.getStatusBar());
            alert.initOwner(owner);
            String apps = map.keySet().stream()
                    .collect(Collectors.joining(", "));
            String txt = String.format(app.getString("msg_terminate"), apps);
            alert.setContentText(txt);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    waitUntilZero(count, () -> {
                        map.keySet().stream().forEach((String applicationName) -> {
                            ProcessUtils.killall(applicationName);
                            app.ready();
                        });
                    });
                }
            });
        }
    }

    /**
     * Rereads the values and updates the ui.
     *
     * @param app app instance
     */
    public static void reread(TKMacTuning app) {
        final AtomicCounter c = new AtomicCounter(L.size());
        L.stream().forEach((plugin) -> {
            plugin.readValue(() -> {
                app.getStatusBar().setMainText(String.format(app.getString("msg_reread"),
                        plugin.getShortDescription()));
                c.decrement();
            }
            );
        });
        waitUntilZero(c, () -> {
            app.ready();
        });
    }
    
    /**
     * Waits until a counter reaches zero and then runs code on the JavaFX
     * Application Thread at some unspecified time in the future
     *
     * @param count counter
     * @param r code to run on the JavaFX Application Thread
     */
    private static void waitUntilZero(AtomicCounter count, Runnable r) {
        Thread t = new Thread(() -> {
            while (count.value() > 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    LOGGER.log(Level.WARNING, "waitUntilZero()", ex);
                }
            }
            Platform.runLater(r);
        });
        t.start();
    }
}
