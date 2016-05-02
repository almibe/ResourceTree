package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.impl.JsonPersistence
import org.almibe.resourcetree.impl.ResourceTree
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
        treeViewResourceTree = new ResourceTree<>()
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, new TypeToken<List<TreeModel<String>>>(){}.getType())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)
        treeViewResourceTree.setItemComparator(String.CASE_INSENSITIVE_ORDER)
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
}
