package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.TimeUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UserPermission {

    private final String node;
    private final long duration;
    private final long sentAt;
    private final UUID sentBy;
    private final String sentFor;

    private UUID removedBy;
    private long removedAt;
    private String removedFor;
    private boolean removed;

    public String getExpireString() {
        if (isPermanent()) {
            return "Never";
        }
        return TimeUtils.formatLongIntoDetailedString((getExpiry() - System.currentTimeMillis()) / 1000);
    }

    public long getExpiry() {
        return (this.sentAt + this.duration);
    }

    public boolean isExpired() {
        if (isPermanent()) return false;
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

}
