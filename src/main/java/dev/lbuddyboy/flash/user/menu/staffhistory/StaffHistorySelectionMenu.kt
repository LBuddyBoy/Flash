package dev.lbuddyboy.flash.user.menu.staffhistory

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.bukkit.*
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@AllArgsConstructor
class StaffHistorySelectionMenu : Menu() {
    var target: UUID? = null
    override fun getTitle(player: Player?): String? {
        return "Staff History: " + UserUtils.formattedName(target)
    }

    override fun getButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(DemotionHistoryButton(target))
        buttons.add(PromotionHistoryButton(target))
        buttons.add(UserButton(target))
        buttons.add(PunishmentSelectButton(28, PunishmentType.KICK, target))
        buttons.add(PunishmentSelectButton(31, PunishmentType.MUTE, target))
        buttons.add(PunishmentSelectButton(32, PunishmentType.BAN, target))
        buttons.add(PunishmentSelectButton(33, PunishmentType.IP_BAN, target))
        buttons.add(PunishmentSelectButton(36, PunishmentType.BLACKLIST, target))
        return buttons
    }

    override fun getSize(player: Player): Int {
        return 45
    }

    override fun autoFill(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class UserButton : Button() {
        private val target: UUID? = null
        override fun getSlot(): Int {
            return 14
        }

        override fun getItem(): ItemStack? {
            val user: User = Flash.instance.userHandler.tryUser(target, true)
            return ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                .setName(UserUtils.formattedName(target))
                .setLore(
                    CC.MENU_BAR,
                    "&gCurrently Staff&7: &f" + if (user.activeRank.isStaff) "&aYes" else "&cNo",
                    "&gPlay Time&7: &f" + TimeUtils.formatLongIntoMMSS(user.getStaffInfo().playTime),
                    "&gJoined Staff Team&7: &f" + user.getStaffInfo().joinedStaffTeamDate,
                    CC.MENU_BAR
                )
                .setDurability(3)
                .setOwner(Flash.instance.cacheHandler.getUserCache().getName(target))
                .create()
        }
    }

    @AllArgsConstructor
    private class PromotionHistoryButton : Button() {
        var target: UUID? = null
        override fun getSlot(): Int {
            return 12
        }

        override fun getItem(): ItemStack? {
            val builder = ItemBuilder(Material.EMERALD_BLOCK).setName("&g&lPromotion History")
            val lore: MutableList<String?> = ArrayList(
                listOf(
                    CC.MENU_BAR
                )
            )
            val user: User = Flash.instance.userHandler.tryUser(target, true)
            if (user.getPromotions().isEmpty()) {
                lore.add("&cNo promotions could be found.")
            }
            for (promotion in user.getPromotions()) {
                lore.add(promotion.promotedFrom + " &f-> " + promotion.promotedTo + " &7(" + promotion.promotedAtDate + ")")
            }
            lore.add(CC.MENU_BAR)
            builder!!.setLore(lore)
            return builder.create()
        }
    }

    @AllArgsConstructor
    private class DemotionHistoryButton : Button() {
        var target: UUID? = null
        override fun getSlot(): Int {
            return 16
        }

        override fun getItem(): ItemStack? {
            val builder = ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&g&lDemotion History")
            val lore: MutableList<String?> = ArrayList(
                listOf(
                    CC.MENU_BAR
                )
            )
            val user: User = Flash.instance.userHandler.tryUser(target, true)
            if (user.getDemotions().isEmpty()) {
                lore.add("&cNo demotions could be found.")
            }
            for (demotion in user.getDemotions()) {
                lore.add(demotion.demotedFrom + " &7(" + demotion.demotedAtDate + ")")
            }
            lore.add(CC.MENU_BAR)
            builder!!.setLore(lore)
            return builder.create()
        }
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
            StaffHistoryMenu(target, type).openMenu(player)
        }
    }
}