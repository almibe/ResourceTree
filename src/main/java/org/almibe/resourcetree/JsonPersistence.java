package org.almibe.resourcetree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import org.almibe.resourcetree.api.ResourceTreePersistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class JsonPersistence<T> implements ResourceTreePersistence<T> {
    private final Gson gson;
    private final File jsonFile;
    private final Type type;
    private boolean loading = false;

    public JsonPersistence(File jsonFile, Type type) {
        this.jsonFile = jsonFile;
        this.type = type;
        this.gson = new Gson();
    }

    public JsonPersistence(File jsonFile, Type type, TypeAdapter<T> typeAdapter) {
        this.jsonFile = jsonFile;
        this.type = type;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        this.gson = gsonBuilder.create();
    }

    @Override
    public void load(ResourceTree<T> resourceTree) {
        if (!jsonFile.exists()) {
            return;
        }
        loading = true;
        try (Reader reader = new FileReader(jsonFile)) {
            List<TreeModel<T>> rootModels = gson.fromJson(reader, type);
            reader.close();

            Map<T, List<TreeModel<T>>> parentMap = new HashMap<>();
            Map<T, List<TreeModel<T>>> nextParentMap = new HashMap<>();

            for (TreeModel<T> treeModel : rootModels) {
                resourceTree.add(treeModel.getNode());
                parentMap.put(treeModel.getNode(), treeModel.getChildren());
            }

            while (listOfListSize(parentMap.values()) > 0) {
                nextParentMap.clear();
                for (T parent : parentMap.keySet()) {
                    for (TreeModel<T> treeModel : parentMap.get(parent)) {
                        nextParentMap.put(treeModel.getNode(), treeModel.getChildren());
                        resourceTree.add(treeModel.getNode(), parent);
                    }
                }
                parentMap.clear();
                parentMap.putAll(nextParentMap);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            loading = false;
        }
    }

    @Override
    public void save(ResourceTree<T> resourceTree) {
        if (loading) return;
        List<TreeModel<T>> modelList = new ArrayList<>();
        List<TreeModel<T>> parents = new ArrayList<>();
        List<TreeModel<T>> nextParents = new ArrayList<>();
        List<List<T>> children = new ArrayList<>();
        List<List<T>> nextChildren = new ArrayList<>();
        for (T t : resourceTree.getRootItems()) {
            modelList.add(new TreeModel<>(t));
            children.add(resourceTree.getChildren(t));
        }
        parents.addAll(modelList);
        while (listOfListSize(children) > 0) {
            nextChildren.clear();
            nextParents.clear();
            for (int i = 0; i < children.size(); i++) {
                for (T t : children.get(i)) {
                    nextChildren.add(resourceTree.getChildren(t));
                    parents.get(i).getChildren().add(new TreeModel<>(t));
                }
                nextParents.addAll(parents.get(i).getChildren());
            }
            parents.clear();
            parents.addAll(nextParents);
            children.clear();
            children.addAll(nextChildren);
        }

        try (FileWriter fileWriter = new FileWriter(jsonFile)) {
            fileWriter.write(gson.toJson(modelList));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private <L> int listOfListSize(Collection<List<L>> lists) {
        int size = 0;
        for (List<L> list : lists) {
            size += list.size();
        }
        return size;
    }
}
