package org.almibe.resourcetree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public interface ResourceTreeItemDisplay<T> {
    StringProperty getName(T t);
    ObjectProperty<Image> getIcon(T t);
}
