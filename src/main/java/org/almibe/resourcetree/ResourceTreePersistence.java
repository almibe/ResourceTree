package org.almibe.resourcetree;

public interface ResourceTreePersistence<T, M> extends ResourceTreeMutation<T> {
    void setModeler(ResourceTreeModeler<T, M> resourceTreeModeler);
}
