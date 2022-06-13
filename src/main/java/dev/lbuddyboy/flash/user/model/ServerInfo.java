package dev.lbuddyboy.flash.user.model;

import dev.lbuddyboy.flash.FlashLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class ServerInfo {

    private final String server;
    private ItemStack[] contents = new ItemStack[36];
    private ItemStack[] armor = new ItemStack[4];

}
