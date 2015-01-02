package org.almibe.resourcetree;

import javafx.scene.image.ImageView;

public interface ResourceTreeItemDisplay<T> {
    String getName(T t);
    ImageView getIcon(T t);
}
