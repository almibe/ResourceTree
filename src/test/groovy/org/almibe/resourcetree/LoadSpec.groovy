import javafx.embed.swing.JFXPanel
import org.almibe.resourcetree.TreeModel
import org.almibe.resourcetree.demo.FolderResource
import org.almibe.resourcetree.demo.Resource
import org.almibe.resourcetree.impl.EqualityModeler
import org.almibe.resourcetree.impl.NullPersistence
import org.almibe.resourcetree.impl.TreeViewResourceTree
import spock.lang.Shared
import spock.lang.Specification

class LoadSpec extends Specification {
    @Shared
    JFXPanel fxPanel = new JFXPanel(); //this is a work around to init the JavaFX toolkit and is never used

    @Shared
    TreeViewResourceTree<Resource> treeViewResourceTree

    def setupSpec() {
        treeViewResourceTree = new TreeViewResourceTree<>()
        treeViewResourceTree.setTreePersistence(new NullPersistence<Resource>())
    }

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
}