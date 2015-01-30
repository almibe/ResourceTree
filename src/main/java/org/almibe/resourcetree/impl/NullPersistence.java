package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.ResourceTreeModeler;
import org.almibe.resourcetree.ResourceTreePersistence;
import org.almibe.resourcetree.TreeModel;

public class NullPersistence<T> implements ResourceTreePersistence<T> {
    @Override public void move(T node, T parent) {}
    @Override public void add(T node) {}
    @Override public void add(T node, T parent) {}
    @Override public void remove(T node) {}
    @Override public void update(T node) {}
    @Override public <M> void load(TreeModel<M> treeModel, ResourceTreeModeler<T, M> resourceTreeModeler) {}
}
