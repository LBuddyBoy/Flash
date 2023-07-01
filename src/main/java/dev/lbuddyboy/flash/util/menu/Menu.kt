package dev.lbuddyboy.flash.util.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.bukkit.Tasks
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

abstract class Menu {
    private var size = 0
    abstract fun getTitle(player: Player?): String?
    abstract fun getButtons(player: Player): List<Button>
    open fun getSize(player: Player): Int {
        val buttons = getButtons(player)
        var highest = 0
        for (button in buttons) {
            if (button.slot <= 0) continue
            if (button.slot - 1 <= highest) continue
            highest = button.slot - 1
        }
        return (Math.ceil((highest + 1).toDouble() / 9.0) * 9.0).toInt()
    }

    fun makeInventory(player: Player): Inventory {
        var title = getTitle(player)
        if (title!!.length > 32) title = title.substring(0, 32)
        val inventory = Bukkit.createInventory(null, getSize(player), title)
        if (autoFill()) {
            for (i in 0 until inventory.size) {
                inventory.setItem(i, autoFillItem())
            }
        }
        for (button in getButtons(player)) {
            if (button.slot <= 0) continue
            inventory.setItem(button.slot - 1, button.item)
        }
        size = inventory.size
        return inventory
    }

    fun openMenu(player: Player) {
        player.openInventory(makeInventory(player))
        openedMenus[player.name] = this
    }

    fun close(player: Player) {
        player.closeInventory()
        openedMenus.remove(player.name)
    }

    open fun update(player: Player) {
        if (size != getSize(player)) {
            close(player)
            openMenu(player)
            return
        }
        val inventory = player.openInventory.topInventory
        inventory.contents = arrayOfNulls(0)
        if (autoFill()) {
            for (i in 0 until inventory.size) {
                inventory.setItem(i, autoFillItem())
            }
        }
        for (button in getButtons(player)) {
            inventory.setItem(button.slot - 1, button.item)
        }
        player.updateInventory()
    }

    open fun autoFill(): Boolean {
        return false
    }

    open fun autoUpdate(): Boolean {
        return false
    }

    fun autoFillItem(): ItemStack? {
        return ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(15).create()
    }

    companion object {
        var openedMenus: MutableMap<String, Menu> = ConcurrentHashMap()

        init {
            Tasks.runTimer({
                for ((key, value) in openedMenus) {
                    if (!value.autoUpdate()) continue
                    val player = Bukkit.getPlayer(key)
                    if (player == null || !player.isOnline) continue
                    value.update(player)
                }
            }, 20, 20)
            Bukkit.getServer().pluginManager.registerEvents(ButtonListener(), Flash.instance)
        }
    }
}