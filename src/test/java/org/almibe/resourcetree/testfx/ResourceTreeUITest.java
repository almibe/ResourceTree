package org.almibe.resourcetree.testfx;

import javafx.scene.Parent;
import org.almibe.resourcetree.impl.TreeViewResourceTree;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

public class ResourceTreeUITest {//extends GuiTest {
    private final TreeViewResourceTree<String> resourceTree =  new TreeViewResourceTree<>();

    public ResourceTreeUITest() {
        resourceTree.getWidget().setId("resourceTree");
    }

//    @Override
    protected Parent getRootNode() {
        return resourceTree.getWidget();
    }

//    @Test
    public void basicCheck() {
  //      click("#resourceTree");
    }
}
