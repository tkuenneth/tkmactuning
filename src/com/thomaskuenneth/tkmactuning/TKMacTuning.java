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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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

    private static final Insets PADDING_1 = new Insets(10, 10, 10, 10);

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

        String[][] plugins = new String[][]{
            {"com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin", "disable-shadow"},
            {"com.thomaskuenneth.tkmactuning.plugin.StringChooserPlugin", "screencapture_type"},
            {"com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin", "AppleShowAllFiles"},
            {"com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin", "ShowHardDrivesOnDesktop"},
            {"com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin", "ShowPathbar"},
            {"com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin", "ShowStatusBar"}
        };
        for (String[] data : plugins) {
            if (data.length != 2) {
                throw new RuntimeException("array must have two elements");
            } else {
                addPlugin(tabPane, data[0], data[1]);
            }
        }

        FlowPane topPane = new FlowPane(Orientation.HORIZONTAL, 10, 0);
        topPane.setPadding(PADDING_1);
        topPane.setAlignment(Pos.BASELINE_RIGHT);
        topPane.getChildren().add(new Button("Hello 1")); // FIXME: temp
        topPane.getChildren().add(new Button("Hello 2")); // FIXME: temp

        FlowPane bottomPane = new FlowPane(Orientation.HORIZONTAL, 10, 0);
        bottomPane.setPadding(PADDING_1);
        bottomPane.setAlignment(Pos.BASELINE_RIGHT);
        bottomPane.getChildren().add(new Button("Hello 1")); // FIXME: temp
        bottomPane.getChildren().add(new Button("Hello 2")); // FIXME: temp

        BorderPane borderPane = new BorderPane(tabPane);
        borderPane.setTop(topPane);
        borderPane.setBottom(bottomPane);
        primaryStage.setScene(new Scene(borderPane, 300, 250));
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

    private void addPlugin(TabPane tabPane, String className, String pluginName) {
        try {
            Class clazz = Class.forName(className);
            Constructor cons = clazz.getConstructor(String.class);
            AbstractPlugin plugin = (AbstractPlugin) cons.newInstance(pluginName);
            String uiCategory = plugin.getUICategory();
            Tab tab = (Tab) tabPane.getProperties().get(uiCategory);
            if (tab == null) {
                tab = new Tab(uiCategory);
                tabPane.getProperties().put(uiCategory, tab);
                tabPane.getTabs().add(tab);
                VBox vbox = new VBox();
                vbox.setPadding(PADDING_1);
                vbox.setSpacing(LayoutConstants.VERTICAL_CONTROL_GAP);
                tab.setContent(vbox);
            }
            VBox root = (VBox) tab.getContent();
            Node node = ComponentBuilder.createComponent(plugin);
            if (node != null) {
                root.getChildren().add(node);
            } else {
                LOGGER.log(Level.SEVERE, "could not create control for plugin {0}({1})",
                        new Object[]{className, pluginName});
            }
        } catch (InstantiationException | ClassNotFoundException |
                NoSuchMethodException | SecurityException |
                InvocationTargetException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, "addPlugin()", ex);
        }
    }
}
