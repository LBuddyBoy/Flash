package dev.lbuddyboy.flash.user.model;

import com.google.gson.JsonObject;
import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class Note {

    private final UUID id;
    private final String title;
    private final String message;
    private final UUID sender;
    private final long sentAt;

    private boolean removed;
    private UUID removedBy;
    private long removedAt;
    private String removedFor;

    public String getRemovedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(removedAt);
    }

    public String getDateSentAt() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(new Date(this.sentAt));
    }

}
