package org.almibe.resourcetree.impl;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileResource implements Resource {
    private String name;
    
    public FileResource(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getIcon() {
        return  new ImageView(new Image(Tree.class.getResourceAsStream("Page_white.png")));
    }
    
}
