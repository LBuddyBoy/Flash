package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.Prefix

class PrefixWeightComparator : Comparator<Prefix> {
    override fun compare(prefix: Prefix, otherPrefix: Prefix): Int {
        return Integer.compare(prefix.weight, otherPrefix.weight)
    }
}