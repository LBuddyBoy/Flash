package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.UserPermission;

import java.util.Comparator;

public class PunishmentRemovedComparator implements Comparator<Punishment> {

    @Override
    public int compare(Punishment punishment, Punishment otherPunishment) {
        return Boolean.compare(punishment.isRemoved(), otherPunishment.isRemoved());
    }
}