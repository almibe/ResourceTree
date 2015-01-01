package org.almibe.resourcetree;

import javafx.scene.Parent;

import java.util.Comparator;

public interface ResourceTree<T> {
    Parent getWidget();
    void setItemNestingRule(NestingRule<T> nestingRule);
    void setItemComparator(Comparator<T> comparator);
    void setItemDisplay(ResourceTreeItemDisplay display);
    boolean move(T node, T parent);
    boolean add(T node);
    boolean add(T node, T parent);
    boolean remove(T node);
    ResourceTreeNode<T> getReadonlyRoot();
}
