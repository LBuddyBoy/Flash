package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum PunishmentType {

    KICK("kick", "kicked", ChatColor.valueOf(FlashLanguage.PUNISHMENT_KICK_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_KICK_COLOR.getString()) + "Kick"),
    WARN("warn", "warned", ChatColor.valueOf(FlashLanguage.PUNISHMENT_WARN_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_WARN_COLOR.getString()) + "Warn"),
    MUTE("mute", "muted", ChatColor.valueOf(FlashLanguage.PUNISHMENT_MUTE_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_MUTE_COLOR.getString()) + "Mute"),
    BAN("ban", "banned", ChatColor.valueOf(FlashLanguage.PUNISHMENT_BAN_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_BAN_COLOR.getString()) + "Ban"),
    IP_BAN("ip-ban", "ip-banned", ChatColor.valueOf(FlashLanguage.PUNISHMENT_IP_BAN_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_IP_BAN_COLOR.getString()) + "IP-Ban"),
    BLACKLIST("blacklisted", "blacklisted", ChatColor.valueOf(FlashLanguage.PUNISHMENT_BLACKLIST_COLOR.getString()), ChatColor.valueOf(FlashLanguage.PUNISHMENT_BLACKLIST_COLOR.getString()) + "Blacklist");

    private final String id;
    private final String format;
    private final ChatColor color;
    private final String display;

    public String getPlural() {
        return this.display + "s";
    }

    public String getKickMessage() {
        if (this == BAN) {
            return FlashLanguage.PUNISHMENT_BAN_KICK_FORMAT.getString();
        } else if (this == IP_BAN) {
            return FlashLanguage.PUNISHMENT_IP_BAN_KICK_FORMAT.getString();
        } else if (this == BLACKLIST) {
            return FlashLanguage.PUNISHMENT_BLACKLIST_KICK_FORMAT.getString();
        }
        return null;
    }

}
