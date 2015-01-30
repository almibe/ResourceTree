package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.ResourceTreeModeler;

/**
 * An instance of ResourceTreeModeler for when the Resource and the persistence model are the same.  This case shouldn't
 * come up often in all but the simplest cases.
 */
public class EqualityModeler<T> implements ResourceTreeModeler<T, T> {
    @Override
    public T toResource(T resourceModel) {
        return resourceModel;
    }

    @Override
    public T toResourceModel(T resource) {
        return resource;
    }
}
