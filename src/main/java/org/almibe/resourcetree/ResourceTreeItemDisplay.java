package org.almibe.resourcetree;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.Node;

public interface ResourceTreeItemDisplay<T> {
    ReadOnlyStringProperty getName(T t);
    ReadOnlyObjectProperty<Node> getGraphic(T t);
}
