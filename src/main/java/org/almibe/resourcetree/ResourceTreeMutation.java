package org.almibe.resourcetree;

//import java.util.Collection;

import java.util.List;

public interface ResourceTreeMutation<T> {
    void move(T node, T parent);
//    void move(Collection<T> nodes, T parent);
    void add(T node);
    void add(T node, T parent);
//    void add(Collection<T> nodes);
//    void add(Collection<T> nodes, T parent);
    void remove(T node);
//    void remove(Collection<T> nodes);
    void update(T node);
//    void update(Collection<T> nodes);
    <M> void load(List<TreeModel<M>> treeModel, ResourceTreeModeler<T, M> resourceTreeModeler);
    void clear();
}
