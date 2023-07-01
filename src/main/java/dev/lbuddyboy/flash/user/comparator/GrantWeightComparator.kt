package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.rank.comparator.RankWeightComparator
import dev.lbuddyboy.flash.user.model.Grant

class GrantWeightComparator : Comparator<Grant> {
    override fun compare(grant: Grant, otherGrant: Grant): Int {
        return RankWeightComparator().compare(grant.rank, otherGrant.rank)
    }
}