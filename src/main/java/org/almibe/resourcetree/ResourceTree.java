package org.almibe.resourcetree;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.Parent;

import java.util.Comparator;
import java.util.List;

public interface ResourceTree<T> {
    Parent getWidget();
    void setItemNestingRule(NestingRule<T> nestingRule);
    void setItemComparator(Comparator<T> comparator);
    void setItemDisplay(ResourceTreeItemDisplay display);
    void setTreePersistence(ResourceTreePersistence treePersistence);
    boolean move(T node, T parent);
    boolean add(T node);
    boolean add(T node, T parent);
    boolean remove(T node);
    void clearSelection();
    T getParent(T t); //TODO make observable
    List<T> getChildren(T t); //TODO make observable
    ReadOnlyListProperty<T> getResources();
}
