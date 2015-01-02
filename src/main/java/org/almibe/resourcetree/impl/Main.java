package org.almibe.resourcetree.impl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.almibe.resourcetree.ResourceTree;

public class Main extends Application {

    ResourceTree<Resource> resourceTree = new TreeViewResourceTree<>(new FolderResource(""), false);
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane sp = new SplitPane();
        FolderResource folder = new FolderResource("Scripts");

        resourceTree.setItemNestingRule(new ResourceNestingRule());
        resourceTree.setItemComparator(new ResourceComparator());
        resourceTree.setItemDisplay(new ResourceItemDisplay());
        resourceTree.add(folder);
        resourceTree.add(new FolderResource("Docs"));
        resourceTree.add(new FileResource("May Report"));
        resourceTree.add(new FileResource("Validation Script"));
        resourceTree.add(new FileResource("Webservice Checker"), folder);
        resourceTree.add(new FolderResource("Agile Docs"));
        resourceTree.add(new FileResource("Agile Manifesto"));

        primaryStage.setScene(new Scene(resourceTree.getWidget()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
