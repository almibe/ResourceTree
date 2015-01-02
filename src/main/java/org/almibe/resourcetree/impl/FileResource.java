package org.almibe.resourcetree.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileResource implements Resource {
    private String name;
    private ImageView icon;
    
    public FileResource(String name) {
        this.name = name;
        this.icon = new ImageView(new Image(FileResource.class.getResourceAsStream("Page_white.png")));
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageView getIcon() {
        return icon;
    }
}
