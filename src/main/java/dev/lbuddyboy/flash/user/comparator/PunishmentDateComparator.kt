package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.Punishment

class PunishmentDateComparator : Comparator<Punishment> {
    override fun compare(punishment: Punishment, otherPunishment: Punishment): Int {
        return java.lang.Long.compare(punishment.sentAt, otherPunishment.sentAt)
    }
}