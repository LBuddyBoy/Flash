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
class NextPageButton<T> : Button() {
    var menu: PagedMenu<T>? = null
    override var slot = 0
    override fun getSlot(): Int {
        return slot
    }

    override fun getItem(): ItemStack? {
        val material = if (menu!!.page < menu!!.maxPages) Material.REDSTONE_TORCH_ON else Material.LEVER
        val name = if (menu!!.page < menu!!.maxPages) "&c&lNext Page" else "&c&lNo Next Page"
        return ItemBuilder(material).setName(name).create()
    }

    override fun action(event: InventoryClickEvent) {
        if (event.click.isLeftClick && menu!!.page < menu!!.maxPages) {
            menu!!.page += 1
            menu!!.openMenu((event.whoClicked as Player))
        }
    }
}