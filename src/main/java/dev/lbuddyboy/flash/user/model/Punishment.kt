package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import lombok.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.util.*

@RequiredArgsConstructor
@Data
class Punishment {
    private val id: UUID? = null
    private val type: PunishmentType? = null
    private val target: UUID? = null
    private val sentBy: UUID? = null
    private val sentAt: Long = 0
    private val duration: Long = 0
    private val sentFor: String? = null
    private val server: String? = null
    private val sentSilent = false
    private val removed = false
    private val removedBy: UUID? = null
    private val removedFor: String? = null
    private val removedAt: Long = 0
    private val removedSilent = false
    fun getExpireString(): String? {
        return if (isPermanent()) {
            "Forever"
        } else TimeUtils.formatLongIntoDetailedString((getExpiry() - System.currentTimeMillis()) / 1000)
    }

    fun getExpiry(): Long {
        return sentAt + duration
    }

    fun isExpired(): Boolean {
        if (isPermanent()) {
            return false
        }
        return if (isRemoved()) false else getExpiry() < System.currentTimeMillis()
    }

    fun isPermanent(): Boolean {
        return duration == Long.MAX_VALUE
    }

    fun getAddedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(sentAt)
    }

    fun getRemovedAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(removedAt)
    }

    fun getExpiresAtDate(): String {
        val sdf = SimpleDateFormat()
        sdf.timeZone = TimeZone.getTimeZone(FlashLanguage.TIMEZONE.string)
        return sdf.format(sentAt + duration)
    }

    fun announce() {
        if (removed) {
            val sender = UserUtils.formattedName(removedBy)
            val target = UserUtils.formattedName(target)
            var format =
                if (sentSilent) FlashLanguage.PUNISHMENT_UNPUNISH_SILENT_BROADCAST_FORMAT.string else FlashLanguage.PUNISHMENT_UNPUNISH_PUBLIC_BROADCAST_FORMAT.string
            format = format
                .replace("%TIME%".toRegex(), if (isPermanent()) "permanently" else "temporarily")
                .replace("%TARGET_COLORED%".toRegex(), target!!)
                .replace("%FORMAT%".toRegex(), "un" + type.getFormat())
                .replace("%SENDER_DISPLAY%".toRegex(), sender!!)
            Bukkit.getConsoleSender().sendMessage(CC.translate(format))
            val finalFormat = format
            Bukkit.getOnlinePlayers().forEach { player: Player ->
                if (!player.hasPermission("flash.staff") && this.isSentSilent()) {
                    return@forEach
                }
                player.sendMessage(CC.translate(finalFormat))
            }
            return
        }
        val sender = UserUtils.formattedName(sentBy)
        val target = UserUtils.formattedName(target)
        var format =
            if (sentSilent) FlashLanguage.PUNISHMENT_PUNISH_SILENT_BROADCAST_FORMAT.string else FlashLanguage.PUNISHMENT_PUNISH_PUBLIC_BROADCAST_FORMAT.string
        format = format
            .replace("%TIME%".toRegex(), if (isPermanent()) "permanently" else "temporarily")
            .replace("%TARGET_COLORED%".toRegex(), target!!)
            .replace("%FORMAT%".toRegex(), type.getFormat())
            .replace("%SENDER_DISPLAY%".toRegex(), sender)
        Bukkit.getConsoleSender().sendMessage(CC.translate(format))
        val finalFormat = format
        Bukkit.getOnlinePlayers().forEach { player: Player ->
            if (!player.hasPermission("flash.staff") && this.isSentSilent()) {
                return@forEach
            }
            player.sendMessage(CC.translate(finalFormat))
        }
    }

    fun format(): String? {
        return CC.translate(
            type!!.kickMessage,
            "%REASON%",
            sentFor!!,
            "%TEMP-FORMAT%",
            FlashLanguage.PUNISHMENT_TEMPORARY_FORMAT.string.replace("%TIME%".toRegex(), getExpireString()!!)
        )
    }
}