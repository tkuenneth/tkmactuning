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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private static final String GROUP = "group_";

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
        // Found here: http://stackoverflow.com/a/17488304/5956451
        tabPane.getStyleClass().add("floating");

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

        FlowPane buttonsPane = new FlowPane(Orientation.HORIZONTAL);
        buttonsPane.setPadding(LayoutConstants.PADDING_1);
        buttonsPane.setHgap(LayoutConstants.HORIZONTAL_CONTROL_GAP);
        buttonsPane.setAlignment(Pos.BASELINE_RIGHT);
        final Button buttonReset = new Button(getString("reset"));
        buttonReset.setOnAction(event -> {
            PluginComponentConnector.reset();
        });
        buttonsPane.getChildren().add(buttonReset);
        final Button buttonApply = new Button(getString("apply"));
        buttonApply.setOnAction(event -> {
            PluginComponentConnector.save();
        });
        buttonsPane.getChildren().add(buttonApply);

        HBox statusbar = new HBox();
        statusbar.getChildren().add(new Label("TODO: set it up"));

        BorderPane borderPane = new BorderPane(tabPane);
        borderPane.setTop(buttonsPane);
        borderPane.setBottom(statusbar);
        primaryStage.setScene(new Scene(borderPane, 300, 250));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
    }

    /**
     * Initiates the startup sequence.
     *
     * @param args arguments from the command line
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets a string from TKMacTuning.properties.
     *
     * @param key key
     * @return a string from TKMacTuning.properties
     */
    public static String getString(String key) {
        return INSTANCE.p.getProperty(key);
    }

    private void addPlugin(TabPane tabPane, String className, String pluginName) {
        try {
            Class clazz = Class.forName(className);
            Constructor cons = clazz.getConstructor(String.class);
            AbstractPlugin plugin = (AbstractPlugin) cons.newInstance(pluginName);
            String primaryUICategory = plugin.getPrimaryUICategory();
            Tab tab = (Tab) tabPane.getProperties().get(primaryUICategory);
            if (tab == null) {
                tab = new Tab(primaryUICategory);
                tabPane.getProperties().put(primaryUICategory, tab);
                tabPane.getTabs().add(tab);
                VBox content = new VBox();
                content.setPadding(LayoutConstants.PADDING_1);
                content.setSpacing(LayoutConstants.VERTICAL_CONTROL_GAP);
                tab.setContent(content);
            }
            VBox content = (VBox) tab.getContent();
            Node node = ComponentBuilder.createComponent(plugin);
            if (node != null) {
                String secondaryUICategory = plugin.getSecondaryUICategory();
                if (AbstractPlugin.ROOT.equals(secondaryUICategory)) {
                    content.getChildren().add(node);
                } else {
                    Pane group = (Pane) tabPane.getProperties().get(GROUP + secondaryUICategory);
                    if (group == null) {
                        group = new VBox(LayoutConstants.VERTICAL_CONTROL_GAP);
                        tabPane.getProperties().put(GROUP + secondaryUICategory, group);
                        Label headline = new Label(secondaryUICategory);
                        group.getChildren().add(headline);
                        content.getChildren().add(group);
                    }
                    group.getChildren().add(node);
                }
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
