package org.almibe.resourcetree.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.almibe.resourcetree.ResourceTree;
import org.almibe.resourcetree.impl.NullPersistence;
import org.almibe.resourcetree.impl.TreeViewResourceTree;

public class Main extends Application {

    ResourceTree<Resource> resourceTree = new TreeViewResourceTree<>();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane sp = new SplitPane();
        FolderResource folder = new FolderResource("Scripts");

        resourceTree.setItemNestingRule(new ResourceNestingRule());
        resourceTree.setItemComparator(new ResourceComparator());
        resourceTree.setItemDisplay(new ResourceItemDisplay());
        resourceTree.setTreePersistence(new NullPersistence<>());
        resourceTree.setTreeEventHandler(new ResourceEventHandler());
        resourceTree.add(folder);
        resourceTree.add(new FolderResource("Docs"));
        resourceTree.add(new FileResource("May Report"));
        resourceTree.add(new FileResource("Validation Script"));
        resourceTree.add(new FileResource("Webservice Checker"), folder);
        resourceTree.add(new FolderResource("Agile Docs"));
        resourceTree.add(new FileResource("Agile Manifesto"));
        FileResource temp = new FileResource("Temp Script");
        resourceTree.add(temp);
        resourceTree.remove(temp);
        resourceTree.add(temp);
        resourceTree.move(temp, folder);

        primaryStage.setScene(new Scene(resourceTree.getWidget()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
