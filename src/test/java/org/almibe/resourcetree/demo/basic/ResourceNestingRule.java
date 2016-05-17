package org.almibe.resourcetree.demo.basic;

import org.almibe.resourcetree.api.NestingRule;

public class ResourceNestingRule implements NestingRule<Resource> {
    @Override
    public boolean canNest(Resource source, Resource target) {
        return target instanceof ParentResource;
    }

    @Override
    public boolean canNestInRoot(Resource node) {
        return true;
    }
}
