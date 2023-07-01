package dev.lbuddyboy.flash.rank.comparator

import dev.lbuddyboy.flash.rank.Rank

class RankWeightComparator : Comparator<Rank?> {
    override fun compare(rank: Rank?, otherRank: Rank?): Int {
        return Integer.compare(rank.getWeight(), otherRank.getWeight())
    }
}