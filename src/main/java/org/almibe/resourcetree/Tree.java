package org.almibe.resourcetree;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class Tree {
    private final TreeItem<Resource> root = new TreeItem<>(new FolderResource(""));
    private final TreeView<Resource> tree = new TreeView<>(root);
    private final Map<Resource, TreeItem<Resource>> resourceToTreeItemMap = new HashMap<>();
    private final ListChangeListener<TreeItem<Resource>> listener = c -> {
        c.next();
        if (tree.getSelectionModel().getSelectedItem() != null && c.getAddedSize() == 1) {
            TreeItem<Resource> ti = c.getAddedSubList().get(0);
            tree.getSelectionModel().select(ti);
        }
    };
    public static final Node createFolderIcon() {
        return new ImageView(
        new Image(Tree.class.getResourceAsStream("Icons-mini-folder.gif")));
    }
    public static final Node createFileIcon() {
        return new ImageView(
        new Image(Tree.class.getResourceAsStream("Page_white.png")));
    }
    
    public Tree() {
        tree.showRootProperty().set(false);
        tree.setCellFactory((TreeView<Resource> tree) -> new DnDCell(tree));
        root.getChildren().addListener(listener);
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
        treeItem.getChildren().addListener(listener);
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
        treeItem.getChildren().addListener(listener);
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
            if (current.getValue().getName().compareTo(child.getValue().getName()) > 0) {
                target.getChildren().add(index, child);
                return;
            }
        }
        target.getChildren().add(child);
    }

    private DnDCell dragSource;
    
    private class DnDCell extends TreeCell<Resource> {
        private Resource item;
        
        public DnDCell(final TreeView<Resource> parentTree) {
            setOnDragDetected((MouseEvent event) -> {
                if (item == null) {
                    return;
                }
                startFullDrag();
                dragSource = (DnDCell)event.getSource();
                event.consume();
            });
//            setOnMouseDragEntered((MouseDragEvent mouseDragEvent) -> {
//                EventTarget et = mouseDragEvent.getTarget();
//                if(et instanceof DnDCell) {
//                    dropTarget = (DnDCell) et;
//                }
//                mouseDragEvent.consume();
//            });
//            setOnMouseDragExited((MouseDragEvent event) -> {
//                dropTarget = null; //TODO does exit always get called before entered?
//                event.consume();
//            });
            setOnMouseDragReleased((MouseDragEvent mouseDragEvent) -> {
                TreeItem<Resource> source = dragSource.getTreeItem();
                TreeItem<Resource> target = this.getTreeItem();
                if(target == null) {
                    target = root;
                }
                if (source == null || target == null || source == target || !(target.getValue() instanceof ParentResource) || isChild(source, target) || target.getChildren().contains(source)) {
                    mouseDragEvent.consume();
                    return;
                }
                source.getParent().getChildren().remove(source);
                addOrdered(target, source);
                mouseDragEvent.consume();
            });
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
                setText(item.getName());
                setGraphic(item.getIcon());
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
