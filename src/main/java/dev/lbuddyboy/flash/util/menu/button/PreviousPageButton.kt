package dev.lbuddyboy.flash.util.menu.button

import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@AllArgsConstructor
class PreviousPageButton<T> : Button() {
    var menu: PagedMenu<T>? = null
    override var slot = 0
    override fun getSlot(): Int {
        return slot
    }

    override fun getItem(): ItemStack? {
        val material = if (menu!!.page > 1) Material.REDSTONE_TORCH_ON else Material.LEVER
        val name = if (menu!!.page > 1) "&c&lPrevious Page" else "&c&lNo Previous Page"
        return ItemBuilder(material).setName(name).create()
    }

    override fun action(event: InventoryClickEvent) {
        if (event.click.isLeftClick && menu!!.page > 1) {
            menu!!.page -= 1
            menu!!.openMenu((event.whoClicked as Player))
        }
    }
}