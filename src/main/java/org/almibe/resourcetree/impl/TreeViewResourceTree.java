package org.almibe.resourcetree.impl;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import org.almibe.resourcetree.NestingRule;
import org.almibe.resourcetree.ResourceTree;
import org.almibe.resourcetree.ResourceTreeEventHandler;
import org.almibe.resourcetree.ResourceTreeItemDisplay;
import org.almibe.resourcetree.ResourceTreePersistence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TreeViewResourceTree<T> implements ResourceTree<T> {
    private final TreeItem<T> root;
    private final TreeView<T> tree;
    private final ObservableMap<T, TreeItem<T>> resourceToTreeItemMap = FXCollections.observableHashMap();
    private final ObservableList<T> resources = FXCollections.observableArrayList();
    private ResourceTreeItemDisplay<T> itemDisplay;
    private Comparator<T> itemComparator;
    private NestingRule<T> itemNestingRule;
    private ResourceTreePersistence<T> treePersistence;
    private ResourceTreeEventHandler<T> treeEventHandler;

    //variables used for DnD
    private DraggableCell dragSource;
    private Image cross = new Image(TreeViewResourceTree.class.getResourceAsStream("xb24.png"));
    private final String selectedStyle = "list-cell-selected";

    public TreeViewResourceTree(T root, boolean showRoot) {
        this.root =  new TreeItem<>(root);
        this.tree = new TreeView<>(this.root);
        tree.showRootProperty().set(showRoot);
        tree.setCellFactory((TreeView<T> tree) -> new DraggableCell(tree));
        String style = this.getClass().getResource("treeview.css").toExternalForm();
        tree.getStylesheets().add(style);
        tree.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                TreeItem<T> node = tree.getSelectionModel().getSelectedItem();
                if (node != null) {
                    treeEventHandler.onOpen(node.getValue());
                }
            }
        });
        resourceToTreeItemMap.addListener(new MapChangeListener<T, TreeItem<T>>() {
            @Override
            public void onChanged(Change<? extends T, ? extends TreeItem<T>> change) {
                if (change.wasRemoved() && !change.wasAdded()) {
                    resources.remove(change.getKey());
                } else {
                    resources.add(change.getKey());
                }
            }
        });
    }

    @Override
    public synchronized void clearSelection() {
        this.tree.getFocusModel().focus(-1);
        this.tree.getSelectionModel().select(-1);
    }

    @Override
    public T getRootItem() {
        return this.root.getValue();
    }

    @Override
    public T getParent(T t) {
        TreeItem<T> treeItem = this.resourceToTreeItemMap.get(t);
        return treeItem.getParent().getValue(); //TODO add checking
    }

    @Override
    public List<T> getChildren(T t) {
        List<T> children = new ArrayList<>();
        TreeItem<T> treeItem = this.resourceToTreeItemMap.get(t);
        treeItem.getChildren().forEach(item -> children.add(item.getValue()));
        return children;
    }

    @Override
    public ReadOnlyListProperty<T> getResources() {
        return new ReadOnlyListWrapper(resources); //TODO this needs testing
    }

    /**
     * Add a resource to the tree, directly under its root.
     * 
     * @param resource The resource to be added to the tree
     */
    @Override
    public synchronized void add(T resource) {
        //TODO add checks
        //return add(node, root.getValue()); TODO do this?
        TreeItem<T> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        addOrdered(root, treeItem);
        this.treePersistence.add(resource);
    }
    
    /**
     * Add a resource to the tree with a specified parent.
     * 
     * @param resource The resource to be added to the tree
     * @param parent The parent of the specified resource
     */
    @Override
    public synchronized void add(T resource, T parent) {
        //TODO add checks
        TreeItem<T> treeItem = new TreeItem<>(resource);
        resourceToTreeItemMap.put(resource, treeItem);
        TreeItem<T> parentTreeItem = resourceToTreeItemMap.get(parent);
        addOrdered(parentTreeItem, treeItem);
        this.treePersistence.add(resource, parent);
    }

    //Note: if this method is too hacky, you can also just use FXCollections.sort with a custom Comparator
    private void addOrdered(TreeItem<T> target, TreeItem<T> child) {
        if (target.getChildren().size() == 0) {
            target.getChildren().add(child);
            return;
        }
        for (int index = 0; index < target.getChildren().size(); index++) {
            TreeItem<T> current = target.getChildren().get(index);
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
    public void setTreePersistence(ResourceTreePersistence treePersistence) {
        this.treePersistence = treePersistence;
    }

    @Override
    public void setTreeEventHandler(ResourceTreeEventHandler eventHandler) {
        this.treeEventHandler = eventHandler;
    }

    @Override
    public synchronized void move(T node, T parent) {
        //TODO add checks
        TreeItem<T> nodeTreeItem = resourceToTreeItemMap.get(node);
        TreeItem<T> newParentTreeItem = resourceToTreeItemMap.get(parent);
        if (isValidDrop(nodeTreeItem, newParentTreeItem)) {
            TreeItem<T> currentParentTreeItem = nodeTreeItem.getParent();
            currentParentTreeItem.getChildren().remove(nodeTreeItem);
            addOrdered(newParentTreeItem, nodeTreeItem);
            this.treePersistence.move(node, parent);
        }
    }

    @Override
    public synchronized void remove(T node) {
        //TODO add checks
        TreeItem<T> treeItem = resourceToTreeItemMap.remove(node);
        TreeItem<T> parent = treeItem.getParent();
        parent.getChildren().remove(treeItem);
        this.treePersistence.remove(node);
    }

    @Override
    public void update(T node) {
        //TODO update display of the node in the tree and add checks
        this.treePersistence.update(node);
    }

    private boolean isValidDrop(TreeItem<T> source, TreeItem<T> target) {
        return itemNestingRule.canNest(source.getValue(), target.getValue()) &&
            !(source == null || target == null || source == target || isChild(source, target) || target.getChildren().contains(source));
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

    private class DraggableCell extends TreeCell<T> {
        private T item;

        public DraggableCell(final TreeView<T> parentTree) {
            setOnMouseClicked(event -> {
                if (this.item == null) {
                    tree.getSelectionModel().clearSelection();
                }
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    TreeItem<T> node = tree.getSelectionModel().getSelectedItem();
                    if (node != null && node.getValue() == this.item) {
                        treeEventHandler.onOpen(node.getValue());
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    ContextMenu contextMenu = new ContextMenu();
                    List<TreeItem<T>> items = tree.getSelectionModel().getSelectedItems();
                    if (items == null || items.size() == 0) {
                        contextMenu.getItems().setAll(treeEventHandler.onContextMenu());
                    } else if (items.size() == 1) {
                        contextMenu.getItems().setAll(treeEventHandler.onContextMenu(items.get(0).getValue()));
                    } else {
                        List<T> resources = new ArrayList<>();
                        items.forEach(it -> resources.add(it.getValue()));
                        contextMenu.getItems().setAll(treeEventHandler.onContextMenu(resources));
                    }
                    contextMenu.show(this, event.getScreenX(), event.getScreenY());
                }
            });
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

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            this.item = item;
            if (item != null && itemDisplay != null) {
                setText(itemDisplay.getName(item));
                setGraphic(itemDisplay.getIcon(item));
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
