package org.almibe.resourcetree;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Tree tree = new Tree();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FolderResource folder = new FolderResource("Scripts");
        tree.addResource(folder);
        tree.addResource(new FolderResource("Docs"));
        tree.addResource(new FileResource("May Report"));
        tree.addResource(new FileResource("Validation Script"));
        tree.addResource(new FileResource("Webservice Checker"), folder);
        tree.addResource(new FolderResource("Agile Docs"));
        tree.addResource(new FileResource("Agile Manifestor"));
        primaryStage.setScene(new Scene(tree.getTree()));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        Main.launch(args);
    }
}
