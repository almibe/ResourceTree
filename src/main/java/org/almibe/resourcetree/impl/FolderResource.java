package org.almibe.resourcetree.impl;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FolderResource implements ParentResource {
    private String name;
    private ImageView icon;

    public FolderResource(String name) {
        this.name = name;
        this.icon = new ImageView(new Image(FolderResource.class.getResourceAsStream("Icons-mini-folder.gif")));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageView getIcon() {
        return  icon;
    }
}
