/*
 * TKMacTuning.java
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
import com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the main class of TKMacTuning.
 *
 * @author Thomas Kuenneth
 */
public class TKMacTuning extends Application {

    private static final Logger LOGGER = Logger.getLogger(TKMacTuning.class.getName());
    private static final TKMacTuning INSTANCE = new TKMacTuning();

    private final Properties p;

    public TKMacTuning() {
        p = new Properties();
        try {
            p.load(getClass().getResourceAsStream("resources/TKMacTuning.properties"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "loading properties file failed", ex);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        addBooleanPlugin(tabPane, "disable-shadow");
        addBooleanPlugin(tabPane, "AppleShowAllFiles");
        primaryStage.setScene(new Scene(tabPane, 300, 250));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ComponentBuilder.save();
    }

    /**
     * Initiates the startup sequence.
     *
     * @param args arguments from the command line
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static String getString(String key) {
        return INSTANCE.p.getProperty(key);
    }

    private void addBooleanPlugin(TabPane tabPane, String pluginName) {
        AbstractPlugin plugin = new BooleanPlugin(pluginName);
        Control c = ComponentBuilder.createComponent(plugin);
        String uiCategory = plugin.getUICategory();
        Tab tab = (Tab) tabPane.getProperties().get(uiCategory);
        if (tab == null) {
            tab = new Tab(uiCategory);
            tabPane.getProperties().put(uiCategory, tab);
            tabPane.getTabs().add(tab);
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10, 10, 10, 10));
            tab.setContent(vbox);
        }
        VBox root = (VBox) tab.getContent();
        root.getChildren().add(c);
    }
}
