package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class PermissionBuild {

    private UUID target;
    private String node;
    private String time;
    private String reason;

}
