package org.almibe.resourcetree;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.almibe.resourcetree.api.ResourceTreePersistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public class JsonPersistence<T> implements ResourceTreePersistence<T> {
    private final File jsonFile;
    private boolean loading = false;
    private final TypeAdapter<T> typeAdapter;

    public JsonPersistence(File jsonFile, TypeAdapter<T> typeAdapter) {
        this.jsonFile = jsonFile;
        this.typeAdapter = typeAdapter;
    }

    @Override
    public void load(ResourceTree<T> resourceTree) {
        if (!jsonFile.exists() || jsonFile.length() == 0) {
            return;
        }
        loading = true;
        try (Reader reader = new FileReader(jsonFile)) {
            JsonReader jsonReader = new JsonReader(reader);

            if (jsonReader.hasNext()) {
                jsonReader.beginArray();
                while (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
                    loadNode(jsonReader, resourceTree, null);
                }
                jsonReader.endArray();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            loading = false;
        }
    }

    private void loadNode(JsonReader jsonReader, ResourceTree<T> resourceTree, T parent) throws Exception{
        jsonReader.beginObject();

        jsonReader.nextName(); //node
        T value = typeAdapter.read(jsonReader);
        resourceTree.add(value, parent);

        jsonReader.nextName(); //children
        jsonReader.beginArray();
        while (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            loadNode(jsonReader, resourceTree, value);
        }
        jsonReader.endArray();

        jsonReader.endObject();
    }

    @Override
    public void save(ResourceTree<T> resourceTree) {
        if (loading) return;

        try (FileWriter fileWriter = new FileWriter(jsonFile)) {
            JsonWriter jsonWriter = new JsonWriter(fileWriter);

            jsonWriter.beginArray();
            resourceTree.getRootItems().forEach(node -> saveNode(jsonWriter, resourceTree, node));
            jsonWriter.endArray();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void saveNode(JsonWriter jsonWriter, ResourceTree<T> resourceTree, T node) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("node");
            typeAdapter.write(jsonWriter, node);
            jsonWriter.name("children");
            jsonWriter.beginArray();
            resourceTree.getChildren(node).forEach(child -> saveNode(jsonWriter, resourceTree, child));
            jsonWriter.endArray();
            jsonWriter.endObject();
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
