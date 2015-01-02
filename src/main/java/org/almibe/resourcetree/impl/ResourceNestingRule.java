package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.NestingRule;

public class ResourceNestingRule implements NestingRule<Resource> {
    @Override
    public boolean canNest(Resource source, Resource target) {
        return target instanceof ParentResource;
    }
}
