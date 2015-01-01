package org.almibe.resourcetree.impl;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class Tree {
    private final TreeItem<Resource> root = new TreeItem<>(new FolderResource(""));
    private final TreeView<Resource> tree = new TreeView<>(root);
    private final Map<Resource, TreeItem<Resource>> resourceToTreeItemMap = new HashMap<>();

    public Tree() {
        tree.showRootProperty().set(false);
        tree.setCellFactory((TreeView<Resource> tree) -> new DraggableCell(tree));
        String style = this.getClass().getResource("treeview.css").toExternalForm();
        tree.getStylesheets().add(style);
    }

    public Parent getTree() {
        return this.tree;
    }
    
    public void clear() {
        this.tree.getFocusModel().focus(-1);
        this.tree.getSelectionModel().select(-1);
    }
    
    /**
     * Add a resource to the tree, directly under its root.
     * 
     * @param resource The resource to be added to the tree
     */
    public void addResource(Resource resource) {
        TreeItem<Resource> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        addOrdered(root, treeItem);
    }
    
    /**
     * Add a resource to the tree with a specified parent.
     * 
     * @param resource The resource to be added to the tree
     * @param parent The parent of the specified resource
     */
    public void addResource(Resource resource, ParentResource parent) {
        TreeItem<Resource> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        TreeItem<Resource> parentTreeItem = resourceToTreeItemMap.get(parent);
        addOrdered(parentTreeItem, treeItem);
    }

    //Note: if this method is too hacky, you can also just use FXCollections.sort with a custom Comparator
    private void addOrdered(TreeItem<Resource> target, TreeItem<Resource> child) {
        if (target.getChildren().size() == 0) {
            target.getChildren().add(child);
            return;
        }
        for (int index = 0; index < target.getChildren().size(); index++) {
            TreeItem<Resource> current = target.getChildren().get(index);
            if (current.getValue() instanceof ParentResource && !(child.getValue() instanceof ParentResource)) {
                continue;
            }
            if (!(current.getValue() instanceof ParentResource) && child.getValue() instanceof ParentResource) {
                target.getChildren().add(index, child);
                return;
            }
            if (current.getValue().getName().getValue().compareTo(child.getValue().getName().getValue()) > 0) {
                target.getChildren().add(index, child);
                return;
            }
        }
        target.getChildren().add(child);
    }

    private void addOrderedDrop(TreeItem<Resource> target, TreeItem<Resource> child) {
        addOrdered(target,child);
        tree.getSelectionModel().select(child);
    }

    private DraggableCell dragSource;
    private Image cross = new Image(Tree.class.getResourceAsStream("xb24.png"));
    private final String selectedStyle = "list-cell-selected";

    private class DraggableCell extends TreeCell<Resource> {
        private Resource item;
        
        public DraggableCell(final TreeView<Resource> parentTree) {
            setOnDragDetected((MouseEvent event) -> {
                if (item == null) {
                    return;
                }
                startFullDrag();
                dragSource = (DraggableCell)event.getSource();
                event.consume();
            });
            setOnMouseDragEntered((MouseDragEvent mouseDragEvent) -> {
                TreeItem<Resource> source = dragSource.getTreeItem();
                TreeItem<Resource> target = this.getTreeItem();
                if(target == null) { target = root; }
                if(isValidDrop(source, target)) {
                    tree.setCursor(Cursor.MOVE);
                    this.getStyleClass().add(selectedStyle);
                } else {
                    tree.setCursor(new ImageCursor(cross, cross.getWidth()/2, cross.getHeight()/2));
                }
                mouseDragEvent.consume();
            });
            setOnMouseDragExited((MouseDragEvent event) -> {
                tree.setCursor(Cursor.DEFAULT);
                this.getStyleClass().remove(selectedStyle);
                event.consume();
            });
            setOnMouseDragReleased((MouseDragEvent mouseDragEvent) -> {
                TreeItem<Resource> source = dragSource.getTreeItem();
                TreeItem<Resource> target = this.getTreeItem();
                tree.setCursor(Cursor.DEFAULT);
                this.getStyleClass().remove(selectedStyle);
                if(target == null) {
                    target = root;
                }
                if (!isValidDrop(source, target)) {
                    mouseDragEvent.consume();
                    return;
                }
                source.getParent().getChildren().remove(source);
                addOrderedDrop(target, source);
                mouseDragEvent.consume();
            });
        }

        private boolean isValidDrop(TreeItem<Resource> source, TreeItem<Resource> target) {
            return !(source == null || target == null || source == target || !(target.getValue() instanceof ParentResource) || isChild(source, target) || target.getChildren().contains(source));
        }

        private boolean isChild(TreeItem<Resource> source, TreeItem<Resource> target) {
            boolean result = false;
            if (source.getChildren().contains(target)) {
                result = true;
            } else {
                for (TreeItem<Resource> child : source.getChildren()) {
                    result = isChild(child, target);
                    if (result == true) {
                        break;
                    }
                }
            }
            return result;
        }
        
        @Override
        protected void updateItem(Resource item, boolean empty) {
            super.updateItem(item, empty);
            this.item = item;
            if (item != null) {
                //textProperty().bindBidirectional(item.getName());
                //graphicProperty().bindBidirectional(item.getIcon());
                setText(item.getName().getValue());
                setGraphic(item.getIcon().getValue());
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
