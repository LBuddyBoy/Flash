package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class GrantBuild {

    private UUID target;
    private UUID rank;
    private String time;
    private String[] scopes;
    private String reason;

    public Rank getRank() {
        return Flash.getInstance().getRankHandler().getRanks().get(rank);
    }

}
