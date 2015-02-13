package org.almibe.resourcetree.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.almibe.resourcetree.ResourceTree;
import org.almibe.resourcetree.ResourceTreeModeler;
import org.almibe.resourcetree.ResourceTreePersistence;
import org.almibe.resourcetree.TreeModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
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
        try (Reader reader = new FileReader(jsonFile)) {
            resourceTree.setTreePersistence(new NullPersistence()); //start with null persistence while loading
            List<TreeModel<M>> rootModels = gson.fromJson(reader, new TypeToken<List<TreeModel<M>>>() {}.getType());
            reader.close();

            List<T> parents = new ArrayList<>();
            List<T> nextParents = new ArrayList<>();
            List<TreeModel<M>> children = new ArrayList<>();
            List<TreeModel<M>> nextChildren = new ArrayList<>();

            for (TreeModel<M> treeModel : rootModels) {
                resourceTree.add(resourceTreeModeler.toResource(treeModel.getNode()));
                children.add(treeModel);
            }

            parents.addAll(resourceTree.getRootItems());

            while (listOfListSizeModel(children) > 0) {
                nextChildren.clear();
                nextParents.clear();
                for (int i = 0; i < children.size(); i++) {
                    for (TreeModel<M> treeModel : children) {
                        nextChildren.addAll(treeModel.getChildren());
                        resourceTree.add(resourceTreeModeler.toResource(treeModel.getNode()));
                    }
                    nextParents.addAll(resourceTree.getChildren(parents.get(i)));
                }
                parents.clear();
                parents.addAll(nextParents);
                children.clear();
                children.addAll(nextChildren);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resourceTree.setTreePersistence(this);
        }
    }

    @Override
    public void clear() {
        writeJsonFile();
    }

    private void writeJsonFile() {
        List<TreeModel<M>> modelList = new ArrayList<>();
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

    private <L> int listOfListSizeModel(List<TreeModel<L>> lists) {
        int size = 0;
        for (TreeModel<L> list : lists) {
            size += list.getChildren().size();
        }
        return size;
    }


    @Override
    public void setModeler(ResourceTreeModeler<T, M> resourceTreeModeler) {
        this.resourceTreeModeler = resourceTreeModeler;
    }
}
