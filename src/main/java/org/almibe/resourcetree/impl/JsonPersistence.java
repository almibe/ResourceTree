package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.ResourceTree;
import org.almibe.resourcetree.ResourceTreeModeler;
import org.almibe.resourcetree.ResourceTreePersistence;
import org.almibe.resourcetree.TreeModel;

import java.io.File;
import java.util.List;

public class JsonPersistence<T> implements ResourceTreePersistence<T> {
    private final File jsonFile;
    private final ResourceTree<T> resourceTree;

    public JsonPersistence(File jsonFile, ResourceTree<T> resourceTree) {
        this.jsonFile = jsonFile;
        this.resourceTree = resourceTree;
    }

    @Override
    public void move(T node, T parent) {
        writeJsonFile();
    }

    @Override
    public void add(T node) {
        writeJsonFile();
    }

    @Override
    public void add(T node, T parent) {
        writeJsonFile();
    }

    @Override
    public void remove(T node) {
        writeJsonFile();
    }

    @Override
    public void update(T node) {
        writeJsonFile();
    }

    @Override
    public void load() {
/*
        if (treeModelList == null) { throw new IllegalArgumentException("treeModel can't be null"); }
        if (resourceTreeModeler == null) { throw new IllegalArgumentException("resourceTreeModeler can't be null"); }

        //clean old values -- is this the only one?
        resourceToTreeItemMap.clear();

        Map<TreeModel<M>, TreeItem<T>> childToParent = new HashMap<>();
        Queue<TreeModel<M>> resourceQueue = new LinkedList<>();

        TreeItem<T> nullRoot = new TreeItem<>();
        this.tree.setRoot(nullRoot);

        for (TreeModel<M> treeModel : treeModelList) {
            T resource = resourceTreeModeler.toResource(treeModel.getNode());

            TreeItem<T> rootTreeItem = new TreeItem(resource);
            resourceToTreeItemMap.put(resource, rootTreeItem);
            this.tree.getRoot().getChildren().add(rootTreeItem);

            if (!treeModel.getChildren().isEmpty()) {
                for (TreeModel<M> childTreeModel : treeModel.getChildren()) {
                    childToParent.put(childTreeModel, rootTreeItem);
                    resourceQueue.add(childTreeModel);
                }
                while (!resourceQueue.isEmpty()) {
                    TreeModel<M> item = resourceQueue.poll();
                    T itemResource = resourceTreeModeler.toResource(item.getNode());
                    TreeItem<T> treeItem = new TreeItem(itemResource);
                    TreeItem<T> parent = childToParent.remove(item);
                    parent.getChildren().add(treeItem);
                    resourceToTreeItemMap.put(itemResource, rootTreeItem);

                    for (TreeModel<M> childTreeModel : item.getChildren()) {
                        childToParent.put(childTreeModel, treeItem);
                        resourceQueue.add(childTreeModel);
                    }
                }
            }
        }

 */
    }

    @Override
    public void clear() {
        writeJsonFile();
    }

    private void writeJsonFile() {

    }
}
