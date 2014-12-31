package org.almibe.resourcetree;

import javafx.beans.property.ReadOnlyListProperty;

public interface ResourceTreeNode<T> {
    T getValue();
    ReadOnlyListProperty<ResourceTreeNode<T>> getChildren();
}
