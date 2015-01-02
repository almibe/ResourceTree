package org.almibe.resourcetree;

public interface ResourceTreePersistence<T> {
    boolean move(T node, T parent);
    boolean add(T node);
    boolean add(T node, T parent);
    boolean remove(T node);
    boolean update(T node);
}
