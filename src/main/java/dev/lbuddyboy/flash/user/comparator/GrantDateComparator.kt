package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.Grant

class GrantDateComparator : Comparator<Grant> {
    override fun compare(grant: Grant, otherGrant: Grant): Int {
        return java.lang.Long.compare(grant.addedAt, otherGrant.addedAt)
    }
}