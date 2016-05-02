package org.almibe.resourcetree;

import org.almibe.resourcetree.api.ResourceTreePersistence;

public class NullPersistence<T, M> implements ResourceTreePersistence<T> {
    @Override public void move(T node, T parent) {}
    @Override public void add(T node) {}
    @Override public void add(T node, T parent) {}
    @Override public void remove(T node) {}
    @Override public void update(T node) {}
    @Override public void load() {}
    @Override public void clear() {}
}
