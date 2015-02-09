package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.demo.FileResource
import org.almibe.resourcetree.demo.Resource
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

    TreeViewResourceTree<Resource> treeViewResourceTree

    File jsonFile

    def setup() {
        jsonFile = temporaryFolder.newFile()
        treeViewResourceTree = new TreeViewResourceTree<>()
        treeViewResourceTree.setTreePersistence(new JsonPersistence<Resource>(jsonFile))
    }

    def 'adding a single node'() {
        when:
        treeViewResourceTree.add(new FileResource("Test"))
        then:
        treeViewResourceTree.rootItems.size() == 1
        Reader reader = new FileReader(jsonFile);
        List<Resource> resources = gson.fromJson(reader, new TypeToken<List<Resource>>(){}.getType());
        println(reader.text)
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
}
