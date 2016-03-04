/*
 * StringPlugin.java
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

import com.thomaskuenneth.tkmactuning.LayoutConstants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * This plugin provides access to string values.
 *
 * @author Thomas Kuenneth
 */
public class StringPlugin extends AbstractPlugin<String> {
    
    private String value;
    private TextField textfield;
    
    public StringPlugin(String plugin) {
        super(plugin);
    }
    
    @Override
    public Class<String> getType() {
        return String.class;
    }
    
    @Override
    public final String getValue() {
        return value;
    }
    
    @Override
    public final void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public Node createNode() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.BASELINE_LEFT);
        hbox.setSpacing(LayoutConstants.LABEL_CONTROL_GAP);
        textfield = new TextField();
        textfield.setOnKeyTyped(event -> {
            setValue(textfield.getText());
        });
        final Label label = new Label(getShortDescription());
        label.setLabelFor(textfield);
        hbox.getChildren().add(label);
        hbox.getChildren().add(textfield);
        return hbox;
    }
    
    @Override
    public void updateNode() {
        textfield.setText(getValue());
    }
}
