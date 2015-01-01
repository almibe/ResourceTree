package org.almibe.resourcetree.impl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.almibe.resourcetree.ResourceTree;

public class Main extends Application {

    Tree tree = new Tree();
    ResourceTree<Resource> resourceTree = new TreeViewResourceTree<>(new FolderResource(""), false);
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane sp = new SplitPane();
        FolderResource folder = new FolderResource("Scripts");
        FolderResource folderNew = new FolderResource("Scripts");

        tree.addResource(folder);
        tree.addResource(new FolderResource("Docs"));
        tree.addResource(new FileResource("May Report"));
        tree.addResource(new FileResource("Validation Script"));
        tree.addResource(new FileResource("Webservice Checker"), folder);
        tree.addResource(new FolderResource("Agile Docs"));
        tree.addResource(new FileResource("Agile Manifesto"));

        resourceTree.setItemNestingRule(new ResourceNestingRule());
        resourceTree.setItemComparator(new ResourceComparator());
        resourceTree.setItemDisplay(new ResourceItemDisplay());
        resourceTree.add(folderNew);
        resourceTree.add(new FolderResource("Docs"));
        resourceTree.add(new FileResource("May Report"));
        resourceTree.add(new FileResource("Validation Script"));
        resourceTree.add(new FileResource("Webservice Checker"), folderNew);
        resourceTree.add(new FolderResource("Agile Docs"));
        resourceTree.add(new FileResource("Agile Manifesto"));

        sp.getItems().addAll(tree.getTree(), resourceTree.getWidget());
        primaryStage.setScene(new Scene(sp));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
