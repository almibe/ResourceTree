package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.embed.swing.JFXPanel
import jdk.nashorn.internal.ir.annotations.Immutable
import org.almibe.resourcetree.api.ResourceTreePersistence
import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

public class JsonPersistenceSpec extends Specification {
    @Shared
    JFXPanel fxPanel = new JFXPanel(); //this is a work around to init the JavaFX toolkit and is never used

    @Shared
    @ClassRule
    TemporaryFolder temporaryFolder

    @Shared
    Gson gson = new Gson();

    ResourceTree<String> treeViewResourceTree
    ResourceTreePersistence<String> resourceTreePersistence;

    File jsonFile

    def setup() {
        jsonFile = temporaryFolder.newFile()
        resourceTreePersistence = new JsonPersistence<>(jsonFile)
        treeViewResourceTree = new ResourceTree<>(null, null, null, resourceTreePersistence, String.CASE_INSENSITIVE_ORDER)
    }

    def 'adding a single node'() {
        when:
        treeViewResourceTree.add("Test")

        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader = new FileReader(jsonFile);
        println(reader.text)
        reader.close();

        then:
        treeViewResourceTree.rootItems.size() == 1
        resources.size() == 1
    }

    def 'add two child nodes'() {
        when:
        treeViewResourceTree.add("Test")
        treeViewResourceTree.add("Test2")

        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader = new FileReader(jsonFile);
        println(reader.text)
        reader.close();

        then:
        treeViewResourceTree.rootItems.size() == 2
        resources.size() == 2
    }

    def 'add nested nodes'() {
        when:
        String test = "Test";
        treeViewResourceTree.add(test)
        treeViewResourceTree.add("Test2")
        treeViewResourceTree.add("ChildTest", test)

        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader = new FileReader(jsonFile);
        println(reader.text)
        reader.close();

        then:
        treeViewResourceTree.rootItems.size() == 2
        treeViewResourceTree.getChildren(treeViewResourceTree.rootItems.get(0)).size() == 1
        treeViewResourceTree.getChildren(treeViewResourceTree.rootItems.get(1)).size() == 0
        resources.size() == 2
        resources.get(0).children.size() == 1
        resources.get(1).children.size() == 0
    }

    def 'test clearing resource tree'() {
        when:
        String test = "Test";
        treeViewResourceTree.add(test)
        treeViewResourceTree.add("Test2")
        treeViewResourceTree.add("ChildTest", test)
        treeViewResourceTree.clear()

        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader = new FileReader(jsonFile);
        println(reader.text)
        reader.close();

        then:
        treeViewResourceTree.rootItems.size() == 0
        resources.size() == 0
    }

    def 'test deep nesting'() {
        when:
        String test = "Test";
        String test2 = "Test2";
        String test3 = "Test3";
        String test4 = "Test4";
        String test5 = "Test5";
        treeViewResourceTree.add(test)
        treeViewResourceTree.add(test2, test)
        treeViewResourceTree.add(test3, test2)
        treeViewResourceTree.add(test4, test3)
        treeViewResourceTree.add(test5, test4)

        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader = new FileReader(jsonFile);
        println(reader.text)
        reader.close();

        then:
        treeViewResourceTree.rootItems.size() == 1
        treeViewResourceTree.getResources().size() == 5
    }

    def 'test JsonPersistence with TypeAdapter'() {
        given:
        ResourceTreePersistence<AdapterTestCase> resourceTreePersistence = new JsonPersistence<>(jsonFile, AdapterTestCase.class, new AdapterTestCaseAdapter())
        ResourceTree<AdapterTestCase> resourceTree = new ResourceTree<>(null, null, null, resourceTreePersistence, String.CASE_INSENSITIVE_ORDER)
        GsonBuilder gsonBuilder = new GsonBuilder()
        gsonBuilder.registerTypeAdapter(AdapterTestCase.class, new AdapterTestCaseAdapter())
        Gson gson = gsonBuilder.create()

        when:
        AdapterTestCase initialValue = new AdapterTestCase(nameProperty: new SimpleStringProperty("Alex"), value: 42.0f)
        resourceTree.add(initialValue)

        Reader reader = new FileReader(jsonFile);
        String initialValueJson = reader.text
        println(initialValueJson)
        reader.close();
        List<TreeModel<AdapterTestCase>> newValue = gson.fromJson(initialValueJson, new TypeToken<List<TreeModel<AdapterTestCase>>>(){}.getType())

        then:
        initialValue.value == newValue.get(0).node.value
        initialValue.nameProperty.value == newValue.get(0).node.nameProperty.value
    }

    @Immutable
    class AdapterTestCase {
        StringProperty nameProperty
        Float value
    }

    class AdapterTestCaseAdapter extends TypeAdapter<AdapterTestCase> {
        @Override
        AdapterTestCase read(final JsonReader jsonReader) {
            def name
            def value

            jsonReader.beginObject()
            while (jsonReader.hasNext()) {
                switch (jsonReader.nextName()) {
                    case "name":
                        name = new SimpleStringProperty(jsonReader.nextString())
                        break
                    case "value":
                        value = (float)jsonReader.nextDouble()
                        break
                }
            }
            jsonReader.endObject()
            return new AdapterTestCase(nameProperty:  name, value: value)
        }

        @Override
        void write(final JsonWriter jsonWriter, AdapterTestCase adapterTestCase) {
            jsonWriter.beginObject()
            jsonWriter.name("name").value(adapterTestCase.nameProperty.value)
            jsonWriter.name("value").value(adapterTestCase.value)
            jsonWriter.endObject()
        }
    }
}
