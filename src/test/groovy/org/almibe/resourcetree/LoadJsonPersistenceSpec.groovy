package org.almibe.resourcetree

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.api.NestingRule
import org.almibe.resourcetree.api.ResourceTreeEventHandler
import org.almibe.resourcetree.api.ResourceTreeItemDisplay
import org.almibe.resourcetree.api.ResourceTreePersistence
import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

import java.lang.reflect.Type

public class LoadJsonPersistenceSpec extends Specification {
    @Shared
    JFXPanel fxPanel = new JFXPanel(); //this is a work around to init the JavaFX toolkit and is never used

    @Shared
    @ClassRule
    TemporaryFolder temporaryFolder

    @Shared
    Gson gson = new Gson();

    @Shared
    Type type = new TypeToken<List<TreeModel<String>>>(){}.getType();

    ResourceTree<String> treeViewResourceTree
    ResourceTreePersistence<String> resourceTreePersistence;

    def setup() {
        treeViewResourceTree = new ResourceTree<>()
        treeViewResourceTree.setItemComparator(String.CASE_INSENSITIVE_ORDER)
        treeViewResourceTree.setItemNestingRule(Stub(NestingRule))
        treeViewResourceTree.setItemDisplay(Stub(ResourceTreeItemDisplay))
        treeViewResourceTree.setTreeEventHandler(Stub(ResourceTreeEventHandler))
    }

    def 'loading a non existent file should not throw an exception'() {
        given:
        File nonExistentFile = new File(temporaryFolder.root,'iDontExist.json')
        JsonPersistence<String, String> persistence = new JsonPersistence<>(nonExistentFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(persistence)

        when:
        treeViewResourceTree.load()

        then:
        notThrown(Exception)
    }

    def 'load json file with empty list'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestEmpty.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 0
    }

    def 'load a single resource'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestSimple.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 1
    }

    def 'load a tree with one level under root'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestNested.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 2
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[0]).size() == 1
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[1]).size() == 0
    }

    def 'load tree with out of order data'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestNestedOutOfOrder.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 2
        treeViewResourceTree.getRootItems().get(0) == 'ATest'
        treeViewResourceTree.getRootItems().get(1) == 'BTest'
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[0]).size() == 0
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems()[1]).size() == 2
    }

    def 'load deeply nested data'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestDeeplyNested.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree, equalityModeler, type)
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 1
        treeViewResourceTree.getResources().size() == 8
        treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems().get(0)).size() == 1
        def child = treeViewResourceTree.getChildren(treeViewResourceTree.getRootItems().get(0)).get(0)
        treeViewResourceTree.getChildren(child).size() == 1
    }
}
