package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.ResourceTreePersistence;

public class NullPersistence<T> implements ResourceTreePersistence<T> {
    @Override public void move(T node, T parent) {}
    @Override public void add(T node) {}
    @Override public void add(T node, T parent) {}
    @Override public void remove(T node) {}
    @Override public void update(T node) {}
}