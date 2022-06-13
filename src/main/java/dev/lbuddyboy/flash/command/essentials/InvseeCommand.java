package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder;
import dev.lbuddyboy.flash.util.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommandAlias("invsee|showinv|showinventory")
@CommandPermission("flash.command.invsee")
public class InvseeCommand extends BaseCommand {

    public static Map<Player, UUID> offlineEditMap = new HashMap<>();

    @Default
    public static void invsee(Player sender, @Name("player") UUID targetUUID) {
        Player target = Bukkit.getPlayer(targetUUID);

/*        if (target == null) {
            User user = Flash.getInstance().getUserHandler().tryUser(targetUUID, true);

            if (user == null) {
                sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
                return;
            }

            offlineEditMap.put(sender, targetUUID);

            Inventory inventory = Bukkit.createInventory(null, 45, CC.translate("&cOffline Inv: " + user.getColoredName()));

            for (int i = 0; i < 36; i++) {
                inventory.setItem(i, user.getServerInfo().getContents()[i]);
            }

            for (int i = 0; i < 9; i++) {
                inventory.setItem(36 + i, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(15).create());
            }

            inventory.setItem(44, user.getServerInfo().getArmor()[0]);
            inventory.setItem(43, user.getServerInfo().getArmor()[1]);
            inventory.setItem(42, user.getServerInfo().getArmor()[2]);
            inventory.setItem(41, user.getServerInfo().getArmor()[3]);

            sender.openInventory(inventory);
            return;
        }*/

        if (sender == target) return;

        sender.openInventory(target.getInventory());
    }

}
