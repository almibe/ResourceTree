package org.almibe.resourcetree;

import java.util.Collection;

public interface ResourceTreeEventHandler<T> {
    void onOpen(T t);
    void onContextMenu();
    void onContextMenu(T t);
    void onContextMenu(Collection<T> t);
}
