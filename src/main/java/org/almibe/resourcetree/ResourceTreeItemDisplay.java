package org.almibe.resourcetree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javax.swing.text.html.ImageView;

public interface ResourceTreeItemDisplay<T> {
    StringProperty getName(T t);
    ObjectProperty<ImageView> getIcon(T t);
}
