package org.almibe.staymanwinesap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Demo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StaymanWinesap tree = new StaymanWinesap();
        primaryStage.setScene(new Scene(tree.getTree()));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        Demo.launch(args);
    }
    
}
