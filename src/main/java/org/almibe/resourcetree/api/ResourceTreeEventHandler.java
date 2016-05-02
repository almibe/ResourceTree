package org.almibe.resourcetree.api;

import javafx.scene.control.MenuItem;

import java.util.Collection;
import java.util.List;

public interface ResourceTreeEventHandler<T> {
    void onOpen(T t);
    List<MenuItem> onContextMenu();
    List<MenuItem> onContextMenu(T t);
    List<MenuItem> onContextMenu(Collection<T> t);
}
