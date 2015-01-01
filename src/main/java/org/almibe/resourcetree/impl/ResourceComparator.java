package org.almibe.resourcetree.impl;

import java.util.Comparator;

public class ResourceComparator implements Comparator<Resource> {
    @Override
    public int compare(Resource o1, Resource o2) {
        return 0;
        /* TODO base comparator off of this method
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
        */
    }
}
