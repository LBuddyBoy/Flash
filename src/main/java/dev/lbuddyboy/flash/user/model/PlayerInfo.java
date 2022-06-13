package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PlayerInfo {

    private boolean pmsOn, claimedNameMC, offlineInventoryEdited;
    private UUID reply;
    private long lastRequestSent, lastMessageSent;
    private List<UUID> readNotifications;

}
