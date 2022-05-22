package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
        return TimeUtils.formatLongIntoDetailedString((getExpiry() - System.currentTimeMillis()) / 1000);
    }

    public long getExpiry() {
        return (this.sentAt + this.duration);
    }

    public boolean isExpired() {
        return getExpiry() < System.currentTimeMillis();
    }

    public String getAddedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(sentAt);
    }

    public String getRemovedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(removedAt);
    }

}
