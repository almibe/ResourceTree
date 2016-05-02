package org.almibe.resourcetree.api;

public interface NestingRule<T> {
    boolean canNest(T node, T parent);
    boolean canNestInRoot(T node);
}
