package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.user.comparator.PunishmentDateComparator;
import dev.lbuddyboy.flash.user.comparator.PunishmentRemovedComparator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
public class StaffInfo {

    private final List<Punishment> knownPunishments = new ArrayList<>();
    private boolean staffNotifications = true, staffChat = false;

    public List<Punishment> getSortedPunishmentsByType(PunishmentType type) {
        return this.knownPunishments.stream().filter(punishment -> punishment.getType() == type).sorted(new PunishmentDateComparator().reversed().thenComparing(new PunishmentRemovedComparator().reversed())).collect(Collectors.toList());
    }

}
