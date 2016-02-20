/*
 * TKMacTuning.java
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
package com.thomaskuenneth.tkmactuning;

import com.thomaskuenneth.tkmactuning.plugin.BooleanPlugin;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
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
        VBox root = new VBox();
        root.getChildren().
                add(ComponentBuilder.createComponent(new BooleanPlugin("disable-shadow")));
        root.getChildren().
                add(ComponentBuilder.createComponent(new BooleanPlugin("AppleShowAllFiles")));
        primaryStage.setScene(new Scene(root, 300, 250));
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
}
