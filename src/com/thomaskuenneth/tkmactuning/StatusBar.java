/*
 * StatusBar.java
 *
 * Copyright 2016 Thomas Kuenneth
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

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This class implements a status bar.
 *
 * @author Thomas Kuenneth
 */
public class StatusBar extends HBox {
    
    private final Label label;
    
    public StatusBar() {
        label = new Label();
        getChildren().add(label);
    }
    
    public void setMainText(String text) {
        label.setText(text);
    }
}
