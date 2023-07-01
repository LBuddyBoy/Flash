package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Promotion {

    private final String promotedFrom;
    private final String promotedTo;
    private final long promotedAt;

    public String getPromotedAtDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(promotedAt);
    }

}
