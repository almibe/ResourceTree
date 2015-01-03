package org.almibe.resourcetree;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.Parent;

import java.util.Comparator;
import java.util.List;

public interface ResourceTree<T> extends ResourceTreeMutation<T> {
    Parent getWidget();
    void setItemNestingRule(NestingRule<T> nestingRule);
    void setItemComparator(Comparator<T> comparator);
    void setItemDisplay(ResourceTreeItemDisplay<T> display);
    void setTreePersistence(ResourceTreePersistence<T> treePersistence);
    void setTreeEventHandler(ResourceTreeEventHandler<T> eventHandler);
    void clearSelection();
    T getParent(T t); //TODO make observable
    List<T> getChildren(T t); //TODO make observable
    ReadOnlyListProperty<T> getResources();
}
