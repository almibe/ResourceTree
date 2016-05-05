package org.almibe.resourcetree;

import org.almibe.resourcetree.api.ResourceTreePersistence;

public class NullPersistence<T> implements ResourceTreePersistence<T> {
    @Override public void load(ResourceTree<T> resourceTree) {}
    @Override public void save(ResourceTree<T> resourceTree) {}
}
