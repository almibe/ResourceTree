package org.almibe.resourcetree;

public interface NestingRule<T> {
    boolean canNest(T node, T parent);
    boolean canNestInRoot(T node);
}
