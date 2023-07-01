package dev.lbuddyboy.flash.server.model;

import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Getter
public abstract class Notification {

    public UUID id;
    public String title, message;
    public long sentAt;

    public String getSentAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(sentAt);
    }

    public abstract void load();
    public abstract void delete();
    public abstract void save();

}
