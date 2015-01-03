package org.almibe.resourcetree;

public interface ResourceTreePersistence<T> {
    void move(T node, T parent);
    void add(T node);
    void add(T node, T parent);
    void remove(T node);
    void update(T node);
}
