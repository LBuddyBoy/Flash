package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class Punishment {

    private final UUID id;
    private final PunishmentType type;
    private final UUID target;
    private final UUID sentBy;
    private final long sentAt, duration;
    private final String sentFor;
    private final String server;
    private final boolean sentSilent;

    private boolean removed;
    private UUID removedBy;
    private String removedFor;
    private long removedAt;
    private boolean removedSilent;

    public String getExpireString() {
        if (isPermanent()) {
            return "Forever";
        }
        return TimeUtils.formatLongIntoDetailedString((getExpiry() - System.currentTimeMillis()) / 1000);
    }

    public long getExpiry() {
        return (this.sentAt + this.duration);
    }

    public boolean isExpired() {
        if (isPermanent()) {
            return false;
        }
        if (isRemoved()) return false;
        return getExpiry() < System.currentTimeMillis();
    }

    public boolean isPermanent() {
        return this.duration == Long.MAX_VALUE;
    }

    public String getAddedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(sentAt);
    }

    public String getRemovedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(removedAt);
    }

    public String getExpiresAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(sentAt + duration);
    }

    public void announce() {
        if (this.removed) {
            String sender = UserUtils.formattedName(this.removedBy);
            String target = UserUtils.formattedName(this.target);

            String format = this.sentSilent ? FlashLanguage.PUNISHMENT_UNPUNISH_SILENT_BROADCAST_FORMAT.getString() : FlashLanguage.PUNISHMENT_UNPUNISH_PUBLIC_BROADCAST_FORMAT.getString();

            format = format
                    .replaceAll("%TIME%", isPermanent() ? "permanently" : "temporarily")
                    .replaceAll("%TARGET_COLORED%", target)
                    .replaceAll("%FORMAT%", "un" + this.type.getFormat())
                    .replaceAll("%SENDER_DISPLAY%", sender);

            Bukkit.getConsoleSender().sendMessage(CC.translate(format));

            String finalFormat = format;

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission("flash.staff") && this.isSentSilent()) {
                    player.sendMessage(CC.translate(finalFormat));
                    return;
                }
                player.sendMessage(CC.translate(finalFormat));
            });

            return;
        }
        String sender = UserUtils.formattedName(this.sentBy);
        String target = UserUtils.formattedName(this.target);

        String format = this.sentSilent ? FlashLanguage.PUNISHMENT_PUNISH_SILENT_BROADCAST_FORMAT.getString() : FlashLanguage.PUNISHMENT_PUNISH_PUBLIC_BROADCAST_FORMAT.getString();

        format = format
                .replaceAll("%TIME%", isPermanent() ? "permanently" : "temporarily")
                .replaceAll("%TARGET_COLORED%", target)
                .replaceAll("%FORMAT%", this.type.getFormat())
                .replaceAll("%SENDER_DISPLAY%", sender);

        Bukkit.getConsoleSender().sendMessage(CC.translate(format));

        String finalFormat = format;

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("flash.staff") && this.isSentSilent()) {
                player.sendMessage(CC.translate(finalFormat));
                return;
            }
            player.sendMessage(CC.translate(finalFormat));
        });

    }

}
