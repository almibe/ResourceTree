package org.almibe.resourcetree.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.almibe.resourcetree.impl.NullPersistence;
import org.almibe.resourcetree.impl.ResourceTree;

import java.util.ArrayList;

public class Main extends Application {

    ResourceTree<Resource> resourceTree = new ResourceTree<>(new ArrayList<>(), new ResourceNestingRule(),
        new ResourceEventHandler(), new ResourceItemDisplay(), new NullPersistence<>(), new ResourceComparator());
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        SplitPane sp = new SplitPane();
        FolderResource folder = new FolderResource("Scripts");

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
