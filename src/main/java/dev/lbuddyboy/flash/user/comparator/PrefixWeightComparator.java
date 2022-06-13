package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.rank.comparator.RankWeightComparator;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Prefix;

import java.util.Comparator;

public class PrefixWeightComparator implements Comparator<Prefix> {

    @Override
    public int compare(Prefix prefix, Prefix otherPrefix) {
        return Integer.compare(prefix.getWeight(), otherPrefix.getWeight());
    }
}
