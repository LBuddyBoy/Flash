package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.comparator.PunishmentDateComparator;
import dev.lbuddyboy.flash.user.comparator.PunishmentRemovedComparator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
public class StaffInfo {

    private final List<Punishment> knownPunishments = new ArrayList<>();
    private boolean staffNotifications = true, staffChat = false;
    private long joinedStaffTeam;
    private long playTime = 1_000L;

    public List<Punishment> getSortedPunishmentsByType(PunishmentType type) {
        return this.knownPunishments.stream().filter(punishment -> punishment.getType() == type).sorted(new PunishmentDateComparator().reversed().thenComparing(new PunishmentRemovedComparator().reversed())).collect(Collectors.toList());
    }

    public String getJoinedStaffTeamDate() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone(FlashLanguage.TIMEZONE.getString()));
        return sdf.format(joinedStaffTeam);
    }

}
