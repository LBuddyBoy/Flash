package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class SearchUsersMenu(var users: List<User?>) : PagedMenu<User?>() {
    init {
        objects = users
    }

    override fun getPageTitle(player: Player?): String? {
        return "Inheritance Editor"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (user in objects!!) {
            buttons.add(UserButton(i++, user, this))
        }
        return buttons
    }

    @AllArgsConstructor
    private class UserButton : Button() {
        override var slot = 0
        var user: User? = null
        var menu: Menu? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                .setName(user!!.coloredName)
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&fClick to view the grants of " + user!!.coloredName + "&f.",
                        CC.MENU_BAR
                    )
                )
                .setDurability(3)
                .setOwner(user.getName())
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            GrantsMenu(user.getUuid(), menu).openMenu(player)
        }
    }
}