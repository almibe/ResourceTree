package org.almibe.resourcetree.demo.basic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.almibe.resourcetree.api.ResourceTreeItemDisplay;


public class ResourceItemDisplay implements ResourceTreeItemDisplay<Resource> {
    @Override
    public StringProperty getName(Resource resource) {
        return new SimpleStringProperty(resource.getName());
    }

    @Override
    public ObjectProperty<Node> getGraphic(Resource resource) {
        return new SimpleObjectProperty<>(resource.getIcon());
    }
}
