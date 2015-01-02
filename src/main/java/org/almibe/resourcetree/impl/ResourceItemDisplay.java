package org.almibe.resourcetree.impl;

import javafx.scene.image.ImageView;
import org.almibe.resourcetree.ResourceTreeItemDisplay;

public class ResourceItemDisplay implements ResourceTreeItemDisplay<Resource> {
    @Override
    public String getName(Resource resource) {
        return resource.getName();
    }

    @Override
    public ImageView getIcon(Resource resource) {
        return resource.getIcon();
    }
}
