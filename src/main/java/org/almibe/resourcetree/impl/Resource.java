package org.almibe.resourcetree.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public interface Resource {
    StringProperty getName();
    ObjectProperty<Node> getIcon();
}
