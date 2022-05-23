package dev.lbuddyboy.flash.rank.comparator;

import dev.lbuddyboy.flash.rank.Rank;

import java.util.Comparator;

public class RankWeightComparator implements Comparator<Rank> {

    @Override
    public int compare(Rank rank, Rank otherRank) {
        return Integer.compare(rank.getWeight(), otherRank.getWeight());
    }
}
