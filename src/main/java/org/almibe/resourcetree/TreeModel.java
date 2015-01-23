package org.almibe.resourcetree;

import java.util.ArrayList;
import java.util.List;

public class TreeModel<T> {
    private final T node;
    private final List<T> children;

    public TreeModel(T node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public TreeModel(T node, List<T> children) {
        this.node = node;
        this.children = children;
    }

    public T getNode() {
        return node;
    }

    public List<T> getChildren() {
        return children;
    }
}
