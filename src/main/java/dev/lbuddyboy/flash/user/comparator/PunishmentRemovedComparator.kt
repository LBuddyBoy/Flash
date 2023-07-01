package dev.lbuddyboy.flash.user.comparator

import dev.lbuddyboy.flash.user.model.Punishment
import java.lang.Boolean
import kotlin.Comparator
import kotlin.Int

class PunishmentRemovedComparator : Comparator<Punishment> {
    override fun compare(punishment: Punishment, otherPunishment: Punishment): Int {
        return Boolean.compare(punishment.isRemoved, otherPunishment.isRemoved)
    }
}