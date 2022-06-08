package dev.lbuddyboy.flash.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PlayerInfo {

    private boolean pmsOn, claimedNameMC;
    private UUID reply;
    private long lastRequestSent, lastMessageSent;

}
