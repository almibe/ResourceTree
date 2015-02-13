package org.almibe.resourcetree.impl;

import com.google.gson.Gson;
import org.almibe.resourcetree.ResourceTree;
import org.almibe.resourcetree.ResourceTreeModeler;
import org.almibe.resourcetree.ResourceTreePersistence;
import org.almibe.resourcetree.TreeModel;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistence<T, M> implements ResourceTreePersistence<T, M> {
    private final Gson gson = new Gson();
    private final File jsonFile;
    private final ResourceTree<T, M> resourceTree;
    private ResourceTreeModeler<T, M> resourceTreeModeler;

    public JsonPersistence(File jsonFile, ResourceTree<T, M> resourceTree) {
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
        List<TreeModel<M>> modelList = new ArrayList<>();
        //convert tree contents to a tree of model objects
        List<TreeModel<M>> parents = new ArrayList<>();
        List<TreeModel<M>> nextParents = new ArrayList<>();
        List<List<T>> children = new ArrayList<>();
        List<List<T>> nextChildren = new ArrayList<>();
        for (T t : resourceTree.getRootItems()) {
            modelList.add(new TreeModel<>(resourceTreeModeler.toResourceModel(t)));
            children.add(resourceTree.getChildren(t));
        }
        parents.addAll(modelList);
        while (listOfListSize(children) > 0) {
            nextChildren.clear();
            nextParents.clear();
            for (int i = 0; i < children.size(); i++) {
                for (T t : children.get(i)) {
                    nextChildren.add(resourceTree.getChildren(t));
                    parents.get(i).getChildren().add(new TreeModel<>(resourceTreeModeler.toResourceModel(t)));
                }
                nextParents.addAll(parents.get(i).getChildren());
            }
            parents.clear();
            parents.addAll(nextParents);
            children.clear();
            children.addAll(nextChildren);
        }
        //persist modelList via gson and write
        if (jsonFile.exists()) {
            try { //TODO replace with try with resource
                FileWriter fileWriter = new FileWriter(jsonFile);
                fileWriter.write(gson.toJson(modelList));
                fileWriter.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try { //TODO replace with try with resource
                jsonFile.createNewFile();
                FileWriter fileWriter = new FileWriter(jsonFile);
                fileWriter.write(gson.toJson(modelList));
                fileWriter.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private <L> int listOfListSize(List<List<L>> lists) {
        int size = 0;
        for (List<L> list : lists) {
            size += list.size();
        }
        return size;
    }

    @Override
    public void setModeler(ResourceTreeModeler<T, M> resourceTreeModeler) {
        this.resourceTreeModeler = resourceTreeModeler;
    }
}
