package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("invsee|showinv|showinventory")
@CommandPermission("flash.command.invsee")
object InvseeCommand : BaseCommand() {
    var offlineEditMap: Map<Player, UUID> = HashMap()
    @Default
    fun invsee(sender: Player, @Name("player") targetUUID: UUID?) {
        val target = Bukkit.getPlayer(targetUUID)

        /*        if (target == null) {
            User user = Flash.instance.getUserHandler().tryUser(targetUUID, true);

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
        }*/if (sender === target) return
        sender.openInventory(target.inventory)
    }
}