package dev.lbuddyboy.flash.user.comparator;

import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.model.UserPermission;

import java.util.Comparator;

public class PunishmentDateComparator implements Comparator<Punishment> {

    @Override
    public int compare(Punishment punishment, Punishment otherPunishment) {
        return Long.compare(punishment.getSentAt(), otherPunishment.getSentAt());
    }
}