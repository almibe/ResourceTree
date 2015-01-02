package org.almibe.resourcetree.impl;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import org.almibe.resourcetree.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TreeViewResourceTree<T> implements ResourceTree<T> {
    private final TreeItem<T> root;
    private final TreeView<T> tree;
    private final Map<T, TreeItem<T>> resourceToTreeItemMap = new HashMap<>();
    private ResourceTreeItemDisplay<T> itemDisplay;
    private Comparator<T> itemComparator;
    private NestingRule<T> itemNestingRule;

    public TreeViewResourceTree(T root, boolean showRoot) {
        this.root =  new TreeItem<>(root);
        this.tree = new TreeView<>(this.root);
        tree.showRootProperty().set(showRoot);
        tree.setCellFactory((TreeView<T> tree) -> new DraggableCell(tree));
        String style = this.getClass().getResource("treeview.css").toExternalForm();
        tree.getStylesheets().add(style);
    }

    @Override
    public void clearSelection() {
        this.tree.getFocusModel().focus(-1);
        this.tree.getSelectionModel().select(-1);
    }
    
    /**
     * Add a resource to the tree, directly under its root.
     * 
     * @param resource The resource to be added to the tree
     */
    @Override
    public boolean add(T resource) {
        //return add(node, root.getValue()); TODO do this?
        TreeItem<T> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        addOrdered(root, treeItem);
        return true; //TODO return real value
    }
    
    /**
     * Add a resource to the tree with a specified parent.
     * 
     * @param resource The resource to be added to the tree
     * @param parent The parent of the specified resource
     */
    @Override
    public boolean add(T resource, T parent) {
        TreeItem<T> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        TreeItem<T> parentTreeItem = resourceToTreeItemMap.get(parent);
        addOrdered(parentTreeItem, treeItem);
        return true; //TODO return real value
    }

    //Note: if this method is too hacky, you can also just use FXCollections.sort with a custom Comparator
    //TODO update to use new Comparator
    private void addOrdered(TreeItem<T> target, TreeItem<T> child) {
        if (target.getChildren().size() == 0) {
            target.getChildren().add(child);
            return;
        }
        for (int index = 0; index < target.getChildren().size(); index++) {
            TreeItem<T> current = target.getChildren().get(index);
//            if (current.getValue() instanceof ParentResource && !(child.getValue() instanceof ParentResource)) {
//                continue;
//            }
//            if (!(current.getValue() instanceof ParentResource) && child.getValue() instanceof ParentResource) {
//                target.getChildren().add(index, child);
//                return;
//            }
//            if (current.getValue().toString().compareTo(child.getValue().toString()) > 0) {
            if (this.itemComparator.compare(current.getValue(), child.getValue()) > 0) {
                target.getChildren().add(index, child);
                return;
            }
        }
        target.getChildren().add(child);
    }

    private void addOrderedDrop(TreeItem<T> target, TreeItem<T> child) {
        addOrdered(target,child);
        tree.getSelectionModel().select(child);
    }

    private DraggableCell dragSource;
    private Image cross = new Image(TreeViewResourceTree.class.getResourceAsStream("xb24.png"));
    private final String selectedStyle = "list-cell-selected";

    @Override
    public Parent getWidget() {
        return this.tree;
    }

    @Override
    public void setItemNestingRule(NestingRule<T> nestingRule) {
        this.itemNestingRule = nestingRule;
    }

    @Override
    public void setItemComparator(Comparator<T> comparator) {
        this.itemComparator = comparator;
    }

    @Override
    public void setItemDisplay(ResourceTreeItemDisplay display) {
        this.itemDisplay = display;
    }

    @Override
    public boolean move(T node, T parent) {
        throw new UnsupportedOperationException("not impl'd");
    }

    @Override
    public boolean remove(T node) {
        throw new UnsupportedOperationException("not impl'd");
    }

    @Override
    public ResourceTreeNode<T> getReadonlyRoot() {
        return null;
    }

    private class DraggableCell extends TreeCell<T> {
        private T item;
        
        public DraggableCell(final TreeView<T> parentTree) {
            setOnDragDetected((MouseEvent event) -> {
                if (item == null) {
                    return;
                }
                startFullDrag();
                dragSource = (DraggableCell)event.getSource();
                event.consume();
            });
            setOnMouseDragEntered((MouseDragEvent mouseDragEvent) -> {
                TreeItem<T> source = dragSource.getTreeItem();
                TreeItem<T> target = this.getTreeItem();
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
                TreeItem<T> source = dragSource.getTreeItem();
                TreeItem<T> target = this.getTreeItem();
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

        private boolean isValidDrop(TreeItem<T> source, TreeItem<T> target) {
            return !(source == null || target == null || source == target || !(target.getValue() instanceof ParentResource) || isChild(source, target) || target.getChildren().contains(source));
        }

        private boolean isChild(TreeItem<T> source, TreeItem<T> target) {
            boolean result = false;
            if (source.getChildren().contains(target)) {
                result = true;
            } else {
                for (TreeItem<T> child : source.getChildren()) {
                    result = isChild(child, target);
                    if (result == true) {
                        break;
                    }
                }
            }
            return result;
        }
        
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            this.item = item;
            if (item != null && itemDisplay != null) {
                //textProperty().bindBidirectional(itemDisplay.getName(item));
                //graphicProperty().bindBidirectional(itemDisplay.getIcon(item));
                setText(itemDisplay.getName(item).getValue());
                setGraphic(itemDisplay.getIcon(item).getValue());
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
