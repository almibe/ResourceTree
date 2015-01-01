package org.almibe.resourcetree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public interface ResourceTreeItemDisplay<T> {
    StringProperty getName(T t);
    ObjectProperty<Node> getIcon(T t);
}
