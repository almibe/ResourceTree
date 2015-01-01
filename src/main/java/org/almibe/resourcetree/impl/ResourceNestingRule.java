package org.almibe.resourcetree.impl;

import org.almibe.resourcetree.NestingRule;

public class ResourceNestingRule implements NestingRule<Resource> {
    @Override
    public boolean canNest(Resource source, Resource target) {
        return !(source == null || target == null || source == target || !(target instanceof ParentResource));
    }
}
