package dev.lbuddyboy.flash.user.menu.staffhistory

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.user.model.Punishment
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.button.BackButton
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class StaffHistoryMenu(var target: UUID?, var type: PunishmentType?) : PagedMenu<Punishment?>() {
    init {
        objects = Flash.instance.userHandler.tryUser(target, true).getStaffInfo().getSortedPunishmentsByType(
            type
        )
    }

    override fun getPageTitle(player: Player?): String? {
        return CC.translate(
            CC.applyTarget(FlashMenuLanguage.PUNISHMENTS_MENU_TITLE.string, target),
            "%PUNISHMENT_PLURAL%",
            type!!.plural
        )
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (punishment in objects!!) {
            if (punishment.getType() != type) continue
            buttons.add(PunishmentButton(i++, punishment))
        }
        return buttons
    }

    override fun autoFill(): Boolean {
        return true
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(BackButton(5, StaffHistorySelectionMenu(target)))
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class PunishmentButton : Button() {
        override var slot = 0
        var punishment: Punishment? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return if (punishment.isRemoved() || punishment!!.isExpired) {
                ItemBuilder(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_MATERIAL.material)
                    .setLore(
                        FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_LORE.stringList,
                        "%ADDEDBY%",
                        UserUtils.formattedName(punishment.getSentBy()),
                        "%ADDEDFOR%",
                        punishment.getSentFor(),
                        "%ADDEDAT%",
                        punishment!!.addedAtDate,
                        "%TIMELEFT%",
                        punishment!!.expireString,
                        "%SERVER%",
                        punishment.getServer(),
                        "%REMOVEDBY%",
                        if (punishment!!.isExpired) "&4Console" else UserUtils.formattedName(punishment.getRemovedBy()),
                        "%REMOVEDFOR%",
                        if (punishment!!.isExpired) "Expired" else punishment.getRemovedFor(),
                        "%REMOVEDAT%",
                        if (punishment!!.isExpired) punishment!!.expiresAtDate else punishment!!.removedAtDate
                    )
                    .setName(
                        FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_NAME.string,
                        "%ADDEDBY%",
                        UserUtils.formattedName(punishment.getSentBy()),
                        "%ADDEDFOR%",
                        punishment.getSentFor(),
                        "%ADDEDAT%",
                        punishment!!.addedAtDate,
                        "%TIMELEFT%",
                        punishment!!.expireString,
                        "%SERVER%",
                        punishment.getServer(),
                        "%REMOVEDBY%",
                        if (punishment!!.isExpired) "&4Console" else UserUtils.formattedName(punishment.getRemovedBy()),
                        "%REMOVEDFOR%",
                        if (punishment!!.isExpired) "Expired" else punishment.getRemovedFor(),
                        "%REMOVEDAT%",
                        if (punishment!!.isExpired) punishment!!.expiresAtDate else punishment!!.removedAtDate
                    )
                    .setDurability(FlashMenuLanguage.PUNISHMENTS_MENU_REMOVED_PUNISHMENT_BUTTON_DATA.int)
                    .create()
            } else ItemBuilder(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_MATERIAL.material)
                .setName(
                    FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_NAME.string,
                    "%ADDEDBY%", UserUtils.formattedName(punishment.getSentBy()),
                    "%SERVER%", punishment.getServer(),
                    "%ADDEDFOR%", punishment.getSentFor(),
                    "%ADDEDAT%", punishment!!.addedAtDate,
                    "%TIMELEFT%", punishment!!.expireString
                )
                .setLore(
                    FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_LORE.stringList,
                    "%ADDEDBY%", UserUtils.formattedName(punishment.getSentBy()),
                    "%SERVER%", punishment.getServer(),
                    "%ADDEDFOR%", punishment.getSentFor(),
                    "%ADDEDAT%", punishment!!.addedAtDate,
                    "%TIMELEFT%", punishment!!.expireString
                )
                .setDurability(FlashMenuLanguage.PUNISHMENTS_MENU_PUNISHMENT_BUTTON_DATA.int)
                .create()
        }

        override fun action(event: InventoryClickEvent) {
//            if (!(event.getWhoClicked() instanceof Player)) return;
//            Player player = (Player) event.getWhoClicked();
//
//            player.closeInventory();
//            PunishmentListener.punishmentRemovePermMap.put(player.getName(), punishment);
//            player.sendMessage(CC.translate("&aType the reason for resolving this punishment."));
        }
    }
}