package dev.lbuddyboy.flash.util.menu

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class ButtonListener : Listener {
    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        if (!Menu.Companion.openedMenus.containsKey(player.name)) return
        val menu: Menu = Menu.Companion.openedMenus.get(player.name)!!
        for (button in menu.getButtons(player)) {
            if (button.slot - 1 == event.slot) {
                button!!.action(event)
                event.isCancelled = button.cancels()
                if (button.clickUpdate()) menu.update(player)
            } else {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        Menu.Companion.openedMenus.remove(event.player.name)
    }
}