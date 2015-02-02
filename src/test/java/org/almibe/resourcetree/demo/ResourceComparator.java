package org.almibe.resourcetree.demo;

import java.util.Comparator;

public class ResourceComparator implements Comparator<Resource> {
    @Override
    public int compare(Resource left, Resource right) {
        if (left == null) { return -1; }
        if (right == null) { return 1; }
        if (left instanceof ParentResource && !(right instanceof ParentResource)) {
            return -1;
        }
        if (!(left instanceof ParentResource) && right instanceof ParentResource) {
            return 1;
        }
        return left.getName().compareTo(right.getName());
    }
}
