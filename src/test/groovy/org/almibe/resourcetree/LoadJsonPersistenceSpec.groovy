package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.impl.EqualityModeler
import org.almibe.resourcetree.impl.JsonPersistence
import org.almibe.resourcetree.impl.TreeViewResourceTree
import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

public class LoadJsonPersistenceSpec extends Specification {
    @Shared
    JFXPanel fxPanel = new JFXPanel(); //this is a work around to init the JavaFX toolkit and is never used

    @Shared
    @ClassRule
    TemporaryFolder temporaryFolder

    @Shared
    Gson gson = new Gson();

    TreeViewResourceTree<String, String> treeViewResourceTree
    ResourceTreePersistence<String, String> resourceTreePersistence;

    def setup() {
        treeViewResourceTree = new TreeViewResourceTree<>()
        treeViewResourceTree.setItemComparator(String.CASE_INSENSITIVE_ORDER)
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

    def 'load non existent file'() {

    }

    def 'load json file with empty list'() {
        given:
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:

        treeViewResourceTree.load()
        then:
        thrown(IllegalArgumentException)
    }

    def 'load a single resource'() {
        given:
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()
        then:
        treeViewResourceTree.getRootItems().size() == 1
    }

    def 'load a tree with one level under root'() {
        given:
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()
        then:
        treeViewResourceTree.getRootItems() == 2
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[0]).size() == 1
    }

}
