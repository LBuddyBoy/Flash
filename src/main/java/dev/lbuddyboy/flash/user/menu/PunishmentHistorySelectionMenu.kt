package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.ColorUtil
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@AllArgsConstructor
class PunishmentHistorySelectionMenu : Menu() {
    var target: UUID? = null
    override fun getTitle(player: Player?): String? {
        return CC.translate(FlashMenuLanguage.PUNISHMENT_SELECTION_MENU_TITLE.string)
    }

    override fun getButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(PunishmentSelectButton(1, PunishmentType.KICK, target))
        buttons.add(PunishmentSelectButton(4, PunishmentType.MUTE, target))
        buttons.add(PunishmentSelectButton(5, PunishmentType.BAN, target))
        buttons.add(PunishmentSelectButton(6, PunishmentType.IP_BAN, target))
        buttons.add(PunishmentSelectButton(9, PunishmentType.BLACKLIST, target))
        return buttons
    }

    override fun autoFill(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class PunishmentSelectButton : Button() {
        override var slot = 0
        var type: PunishmentType? = null
        var target: UUID? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setName(
                    CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_NAME.string, target),
                    "%PUNISHMENT_TYPE%",
                    type.getDisplay()
                )
                .setLore(
                    CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_SELECTION_SELECT_BUTTON_LORE.stringList, target),
                    "%PUNISHMENT_PLURAL%",
                    type!!.plural
                )
                .setDurability(ColorUtil.COLOR_MAP!![type.getColor()]!!.woolData.toShort())
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            PunishmentHistoryMenu(target, type).openMenu(player)
        }
    }
}