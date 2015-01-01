package org.almibe.resourcetree.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FolderResource implements ParentResource {
    private SimpleStringProperty name;
    private SimpleObjectProperty<Node> icon;

    public FolderResource(String name) {
        this.name = new SimpleStringProperty(name);
        this.icon = new SimpleObjectProperty<>(new ImageView(new Image(Tree.class.getResourceAsStream("Icons-mini-folder.gif"))));
    }

    @Override
    public StringProperty getName() {
        return name;
    }

    @Override
    public ObjectProperty<Node> getIcon() {
        return  icon;
    }
}
