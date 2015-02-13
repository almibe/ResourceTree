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

public class JsonPersistenceSpec extends Specification {
    @Shared
    JFXPanel fxPanel = new JFXPanel(); //this is a work around to init the JavaFX toolkit and is never used

    @Shared
    @ClassRule
    TemporaryFolder temporaryFolder

    @Shared
    Gson gson = new Gson();

    TreeViewResourceTree<String, String> treeViewResourceTree
    ResourceTreePersistence<String, String> resourceTreePersistence;

    File jsonFile

    def setup() {
        jsonFile = temporaryFolder.newFile()
        treeViewResourceTree = new TreeViewResourceTree<>()
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)
    }

    def 'adding a single node'() {
        when:
        treeViewResourceTree.add("Test")
        then:
        treeViewResourceTree.rootItems.size() == 1
        Reader reader = new FileReader(jsonFile);
        List<TreeModel<String>> resources = gson.fromJson(reader, new TypeToken<List<TreeModel<String>>>(){}.getType());
        reader.close();
        resources.size() == 1
    }
//
//    def 'add two child nodes'() {
//        given:
//
//        when:
//
//        then:
//        treeViewResourceTree.rootItems.size() == 2
//    }
//
//    def 'add nested nodes'() {
//        given:
//
//        when:
//
//        then:
//        treeViewResourceTree.rootItems.size() == 2
//        treeViewResourceTree.getChildren(treeViewResourceTree.rootItems.get(0)).size() == 0
//        treeViewResourceTree.getChildren(treeViewResourceTree.rootItems.get(1)).size() == 1
//    }
//
//    def 'test clearing resource tree'() {
//
//    }

    /* LOADING TESTS
    def 'load null resource model should throw illegal arguments exception'() {
        when:
        treeViewResourceTree.load(null, new EqualityModeler<Resource>())
        then:
        thrown(IllegalArgumentException)
    }

    def 'load a single resource'() {
        given:
        FolderResource newRoot = new FolderResource("Root")
        TreeModel<Resource> treeModel = new TreeModel(newRoot)
        when:
        treeViewResourceTree.load([treeModel], new EqualityModeler<Resource>())
        then:
        treeViewResourceTree.getRootItems() == [newRoot]
    }

    def 'load a tree with one level under root'() {
        given:
        FolderResource newRoot = new FolderResource("Root")
        TreeModel<Resource> treeModel = new TreeModel(newRoot, [new TreeModel<Resource>(new FolderResource("Child")), new TreeModel<Resource>(new FolderResource("Child"))])
        when:
        treeViewResourceTree.load([treeModel], new EqualityModeler<Resource>())
        then:
        treeViewResourceTree.getRootItems() == [newRoot]
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[0]).size() == 2
    }

     */
}