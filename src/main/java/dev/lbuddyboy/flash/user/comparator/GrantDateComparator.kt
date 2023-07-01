package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.Grant;

import java.util.Comparator;

public class GrantDateComparator implements Comparator<Grant> {

    @Override
    public int compare(Grant grant, Grant otherGrant) {
        return Long.compare(grant.getAddedAt(), otherGrant.getAddedAt());
    }
}