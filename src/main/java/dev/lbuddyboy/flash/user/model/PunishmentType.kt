package dev.lbuddyboy.flash.user.model

import dev.lbuddyboy.flash.FlashLanguage
import lombok.AllArgsConstructor
import lombok.Getter
import org.bukkit.ChatColor

@AllArgsConstructor
@Getter
enum class PunishmentType {
    KICK(
        "kick",
        "kicked",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_KICK_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_KICK_COLOR.string).toString() + "Kick"
    ),
    WARN(
        "warn",
        "warned",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_WARN_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_WARN_COLOR.string).toString() + "Warn"
    ),
    MUTE(
        "mute",
        "muted",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_MUTE_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_MUTE_COLOR.string).toString() + "Mute"
    ),
    BAN(
        "ban",
        "banned",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_BAN_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_BAN_COLOR.string).toString() + "Ban"
    ),
    IP_BAN(
        "ip-ban",
        "ip-banned",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_IP_BAN_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_IP_BAN_COLOR.string).toString() + "IP-Ban"
    ),
    BLACKLIST(
        "blacklisted",
        "blacklisted",
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_BLACKLIST_COLOR.string),
        ChatColor.valueOf(FlashLanguage.PUNISHMENT_BLACKLIST_COLOR.string).toString() + "Blacklist"
    );

    private val id: String? = null
    private val format: String? = null
    private val color: ChatColor? = null
    private val display: String? = null
    fun getPlural(): String {
        return display + "s"
    }

    fun getKickMessage(): String? {
        if (this == BAN) {
            return FlashLanguage.PUNISHMENT_BAN_KICK_FORMAT.string
        } else if (this == IP_BAN) {
            return FlashLanguage.PUNISHMENT_IP_BAN_KICK_FORMAT.string
        } else if (this == BLACKLIST) {
            return FlashLanguage.PUNISHMENT_BLACKLIST_KICK_FORMAT.string
        } else if (this == KICK) {
            return FlashLanguage.PUNISHMENT_KICK_FORMAT.string
        }
        return null
    }
}