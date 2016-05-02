package org.almibe.resourcetree.demo;

import javafx.scene.control.MenuItem;
import org.almibe.resourcetree.api.ResourceTreeEventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ResourceEventHandler implements ResourceTreeEventHandler<Resource> {
    @Override
    public void onOpen(Resource resource) {
        System.out.println(" in onOpen " + resource.getName());
    }

    @Override
    public List<MenuItem> onContextMenu() {
        return Arrays.asList(new MenuItem("treeview content menu"));
    }

    @Override
    public List<MenuItem> onContextMenu(Resource resource) {
        return Arrays.asList(new MenuItem(resource.getName() + " content menu"));
    }

    @Override
    public List<MenuItem> onContextMenu(Collection<Resource> t) {
        List<MenuItem> items = new ArrayList<>();
        t.forEach(it -> items.add(new MenuItem(it.getName())));
        return items;
    }
}
