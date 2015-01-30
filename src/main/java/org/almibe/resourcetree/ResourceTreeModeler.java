package org.almibe.resourcetree;

public interface ResourceTreeModeler<R, M> {
    R toResource(M resourceModel);
    M toResourceModel(R resource);
}
