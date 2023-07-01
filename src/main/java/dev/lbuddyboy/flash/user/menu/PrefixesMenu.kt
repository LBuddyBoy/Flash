package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.comparator.PrefixWeightComparator
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.stream.Collectors

class PrefixesMenu : PagedMenu<Prefix?>() {
    init {
        objects = ArrayList<Any?>(Flash.instance.userHandler.getPrefixes())
    }

    override fun getPageTitle(player: Player?): String? {
        return "All Prefixes"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (prefix in objects!!.stream().sorted(PrefixWeightComparator().reversed()).collect(Collectors.toList())) {
            buttons.add(PrefixButton(i++, prefix, player))
        }
        return buttons
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(ResetPrefixButton(5))
        return buttons
    }

    override fun autoFill(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class ResetPrefixButton : Button() {
        override var slot = 0
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&cReset Prefix &7(Click)").create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            val user: User = Flash.instance.userHandler.tryUser(player.uniqueId, false)
            if (user.getActivePrefix() == null) return
            user.setActivePrefix(null)
            user.save(true)
            player.sendMessage(CC.translate("&aSuccessfully reset your prefix."))
        }
    }

    @AllArgsConstructor
    private class PrefixButton : Button() {
        override var slot = 0
        var prefix: Prefix? = null
        var player: Player? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            val user: User = Flash.instance.userHandler.tryUser(
                player!!.uniqueId, false
            )
            val userPrefix = user.activeRank.getPrefix()
            val userSuffix = user.activeRank.getSuffix()
            return ItemBuilder(Material.PAPER).setName("&6" + prefix.getId() + " &ePrefix")
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&eExample&7: &f" + prefix.getDisplay() + userPrefix + user.coloredName + userSuffix,
                        " ",
                        "&7Click to &aactivate&7 the &6" + prefix.getId() + "&e Prefix&7.",
                        CC.MENU_BAR
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (!player!!.hasPermission("flash.prefix." + prefix.getId())) {
                player!!.sendMessage(CC.translate("&cNo permission."))
                return
            }
            val user: User = Flash.instance.userHandler.tryUser(
                player!!.uniqueId, false
            )
            if (user.getActivePrefix() === prefix) return
            user.setActivePrefix(prefix)
            user.save(true)
            player!!.sendMessage(CC.translate("&aSuccessfully applied the " + prefix.getId() + " prefix."))
        }
    }
}