package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class Grant {

    private final UUID uuid;
    private final UUID rank;
    private final String rankName;
    private final UUID addedBy;
    private final String addedReason;
    private final long addedAt;
    private final long duration;
    private final String[] scopes;

    private UUID removedBy;
    private long removedAt;
    private String removedFor;

    public Rank getRank() {
        return Flash.getInstance().getRankHandler().getRanks().get(this.rank);
    }

    public String getRankName() {
        return getRank() == null ? this.rankName : getRank().getName();
    }

    public boolean isRemoved() {
        return removedBy != null;
    }

    public boolean isExpired() {
        return getExpiresAt() <= 0;
    }

    public long getExpiresAt() {
        return (this.addedAt + this.duration) - System.currentTimeMillis();
    }

    public String getExpireString() {
        if (duration == Long.MAX_VALUE) return "Never";
        return TimeUtils.formatLongIntoDetailedString(getExpiresAt() / 1000);
    }

    public String getAddedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(addedAt);
    }

    public String getRemovedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(removedAt);
    }

    public static Grant defaultGrant() {
        Rank actual = Flash.getInstance().getRankHandler().getDefaultRank();
        return new Grant(UUID.randomUUID(), actual.getUuid(), actual.getName(), null, "Default Grant", System.currentTimeMillis(), Long.MAX_VALUE, new String[]{"GLOBAL"});
    }

}
