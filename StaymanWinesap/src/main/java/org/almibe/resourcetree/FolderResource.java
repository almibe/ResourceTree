package org.almibe.resourcetree;

import javafx.scene.Node;

public class FolderResource implements ParentResource {

    private String name;
    
    public FolderResource(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getIcon() {
        return Tree.createFolderIcon();
    }
    
}
