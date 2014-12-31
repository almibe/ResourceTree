package org.almibe.resourcetree.impl;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FolderResource implements ParentResource {

    private String name;
    
    public FolderResource(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getIcon() {
        return  new ImageView(new Image(Tree.class.getResourceAsStream("Icons-mini-folder.gif")));
    }
    
}
