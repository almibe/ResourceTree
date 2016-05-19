package org.almibe.resourcetree;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
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
        if (!jsonFile.exists()) {
            return;
        }
        loading = true;
        try (Reader reader = new FileReader(jsonFile)) {
            JsonReader jsonReader = new JsonReader(reader);
            //TODO complete

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            loading = false;
        }
    }

    @Override
    public void save(ResourceTree<T> resourceTree) {
        if (loading) return;

        try (FileWriter fileWriter = new FileWriter(jsonFile)) {
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            //TODO complete

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
