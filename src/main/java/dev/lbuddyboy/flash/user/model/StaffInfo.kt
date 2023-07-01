package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.comparator.PunishmentDateComparator
import dev.lbuddyboy.flash.user.comparator.PunishmentRemovedComparator
import lombok.Data
import lombok.RequiredArgsConstructor
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

@RequiredArgsConstructor
@Data
class StaffInfo {
    private val knownPunishments: List<Punishment> = ArrayList()
    private val staffNotifications = true
    private val staffChat = false
    private val joinedStaffTeam: Long = 0
    private val playTime = 1000L
    fun getSortedPunishmentsByType(type: PunishmentType): List<Punishment> {
        return knownPunishments.stream().filter { punishment: Punishment -> punishment.type == type }
            .sorted(PunishmentDateComparator().reversed().thenComparing(PunishmentRemovedComparator().reversed()))
            .collect(Collectors.toList())
    }

    fun getJoinedStaffTeamDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(joinedStaffTeam)
    }
}