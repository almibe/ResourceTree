package org.almibe.resourcetree.api;

import org.almibe.resourcetree.ResourceTree;

public interface ResourceTreePersistence<T> {
    void save(ResourceTree<T> resourceTree);
    void load(ResourceTree<T> resourceTree);
}
