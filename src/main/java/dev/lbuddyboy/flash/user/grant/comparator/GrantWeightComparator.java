package dev.lbuddyboy.flash.user.grant.comparator;

import dev.lbuddyboy.flash.rank.comparator.RankWeightComparator;
import dev.lbuddyboy.flash.user.model.Grant;

import java.util.Comparator;

public class GrantWeightComparator implements Comparator<Grant> {

    @Override
    public int compare(Grant grant, Grant otherGrant) {
        return new RankWeightComparator().compare(grant.getRank(), otherGrant.getRank());
    }
}
