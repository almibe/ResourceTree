package org.almibe.resourcetree.testfx;

import javafx.scene.Parent;
import org.almibe.resourcetree.impl.ResourceTree;

public class ResourceTreeUITest {//extends GuiTest {
    private final ResourceTree<String> resourceTree =  new ResourceTree<>();

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
