package org.almibe.resourcetree;

import javafx.scene.Node;

public class FileResource implements Resource {
    private String name;
    
    public FileResource(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getIcon() {
        return Tree.createFileIcon();
    }
    
}
