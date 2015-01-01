package org.almibe.resourcetree.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.almibe.resourcetree.ResourceTreeItemDisplay;

public class ResourceItemDisplay implements ResourceTreeItemDisplay<Resource> {
    @Override
    public StringProperty getName(Resource resource) {
        return resource.getName();
    }

    @Override
    public ObjectProperty<Node> getIcon(Resource resource) {
        return resource.getIcon();
    }
}
