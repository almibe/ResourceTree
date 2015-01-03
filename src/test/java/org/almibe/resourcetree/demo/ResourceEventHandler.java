package org.almibe.resourcetree.demo;

import org.almibe.resourcetree.ResourceTreeEventHandler;

import java.util.Collection;

public class ResourceEventHandler implements ResourceTreeEventHandler<Resource> {
    @Override
    public void onOpen(Resource resource) {
        System.out.println(" in onOpen " + resource.getName());
    }

    @Override
    public void onContextMenu() {
        System.out.println(" in onContextMenu");
    }

    @Override
    public void onContextMenu(Resource resource) {
        System.out.println(" in onContextMenu " + resource.getName());
    }

    @Override
    public void onContextMenu(Collection<Resource> t) {
        StringBuilder sb = new StringBuilder();
        t.forEach(it -> sb.append(" " + it.getName()));
        System.out.println(" in onOpen " + sb);
    }
}
