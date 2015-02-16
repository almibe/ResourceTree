package org.almibe.resourcetree

import com.google.gson.Gson
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
        treeViewResourceTree.setItemNestingRule(Stub(NestingRule))
        treeViewResourceTree.setItemDisplay(Stub(ResourceTreeItemDisplay))
        treeViewResourceTree.setTreeEventHandler(Stub(ResourceTreeEventHandler))
    }

    def 'loading a non existent file should throw an error'() {
        given:
        File nonExistentFile = new File(temporaryFolder.root,'iDontExist.json')
        JsonPersistence<String, String> persistence = new JsonPersistence<>(nonExistentFile, treeViewResourceTree)
        treeViewResourceTree.setTreePersistence(persistence)

        when:
        treeViewResourceTree.load()

        then:
        thrown(Exception)
    }

    def 'load json file with empty list'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestEmpty.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
        treeViewResourceTree.setTreePersistence(resourceTreePersistence)

        when:
        treeViewResourceTree.load()

        then:
        treeViewResourceTree.getRootItems().size() == 0
    }

    def 'load a single resource'() {
        given:
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestSimple.json").toURI())
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
        File jsonFile = new File(LoadJsonPersistenceSpec.class.getClassLoader().getResource("org/almibe/resourcetree/LoadTestNested.json").toURI())
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
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
        resourceTreePersistence = new JsonPersistence<>(jsonFile, treeViewResourceTree)
        resourceTreePersistence.setModeler(new EqualityModeler<String>())
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
}
