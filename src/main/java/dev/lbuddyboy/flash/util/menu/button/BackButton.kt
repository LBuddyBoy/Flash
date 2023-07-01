package dev.lbuddyboy.flash.util.menu.button

import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@AllArgsConstructor
class BackButton : Button() {
    override var slot = 0
    var menu: Menu? = null
    override fun getSlot(): Int {
        return slot
    }

    override fun getItem(): ItemStack? {
        return ItemBuilder(Material.ARROW)
            .setName("&eGo Back")
            .create()
    }

    override fun action(event: InventoryClickEvent) {
        menu!!.openMenu(event.whoClicked as Player)
    }
}