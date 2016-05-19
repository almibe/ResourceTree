package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import groovy.transform.Immutable
import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.api.NestingRule
import org.almibe.resourcetree.api.ResourceTreeEventHandler
import org.almibe.resourcetree.api.ResourceTreeItemDisplay
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
        resourceTreePersistence = new JsonPersistence<>(jsonFile, new TypeAdapter<String>() {
            @Override
            void write(JsonWriter out, String value) throws IOException {
                out.value(value)
            }

            @Override
            String read(JsonReader jsonReader) throws IOException {
                return jsonReader.nextString()
            }
        })
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
        ResourceTreePersistence<Player> resourceTreePersistence = new JsonPersistence<>(jsonFile, new PlayerAdapter())
        ResourceTree<Player> resourceTree = new ResourceTree<>(null, null, null, resourceTreePersistence, String.CASE_INSENSITIVE_ORDER)
        GsonBuilder gsonBuilder = new GsonBuilder()
        gsonBuilder.registerTypeAdapter(Player.class, new PlayerAdapter())
        Gson gson = gsonBuilder.create()

        when:
        Player initialValue = new Player(name: "Alex", score: 42.0f)
        resourceTree.add(initialValue)

        Reader reader = new FileReader(jsonFile);
        String initialValueJson = reader.text
        println(initialValueJson)
        reader.close();
        List<TreeModel<Player>> newValue = gson.fromJson(initialValueJson, new TypeToken<List<TreeModel<Player>>>(){}.getType())

        then:
        initialValue.score == newValue.get(0).node.score
        initialValue.name == newValue.get(0).node.name
    }

    def 'test loading JsonPersistence with TypeAdapter'() {
        given:
        ResourceTreePersistence<Player> resourceTreePersistence = new JsonPersistence<>(jsonFile, new PlayerAdapter())
        ResourceTree<Player> resourceTree = new ResourceTree<>(Stub(NestingRule), Stub(ResourceTreeEventHandler), Stub(ResourceTreeItemDisplay), resourceTreePersistence, new PlayerComparator())

        when:
        Player initialValue = new Player(name: "Alex", score: 42.0f)
        resourceTree.add(initialValue)
        resourceTree.load()

        then:
        initialValue.score == resourceTree.getRootItems().first().score
        initialValue.name == resourceTree.getRootItems().first().name
    }

    @Immutable
    class Player {
        String name
        Float score
    }

    class PlayerComparator implements Comparator<Player> {
        @Override
        int compare(Player o1, Player o2) {
            return Float.compare(o1.score, o2.score)
        }
    }

    class PlayerAdapter extends TypeAdapter<Player> {
        @Override
        Player read(final JsonReader jsonReader) {
            def name
            def score

            jsonReader.beginObject()

            jsonReader.nextName() //name

            name = jsonReader.nextString()
            jsonReader.nextName() //score
            score = (float)jsonReader.nextDouble()
            jsonReader.endObject()
            return new Player(name:  name, score: score)
        }

        @Override
        void write(final JsonWriter jsonWriter, Player adapterTestCase) {
            jsonWriter.beginObject()
            jsonWriter.name("name").value(adapterTestCase.name)
            jsonWriter.name("score").value(adapterTestCase.score)
            jsonWriter.endObject()
        }
    }
}
