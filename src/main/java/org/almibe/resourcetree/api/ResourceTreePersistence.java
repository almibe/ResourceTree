package org.almibe.resourcetree.api;

public interface ResourceTreePersistence<T> {
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
    void load();
    void clear();
}
