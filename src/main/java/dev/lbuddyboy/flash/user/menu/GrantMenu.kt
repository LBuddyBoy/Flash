package dev.lbuddyboy.flash.user.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashMenuLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.listener.GrantListener
import dev.lbuddyboy.flash.user.model.GrantBuild
import dev.lbuddyboy.flash.user.model.PermissionBuild
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.ColorUtil
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import dev.lbuddyboy.flash.util.menu.button.BackButton
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@AllArgsConstructor
class GrantMenu : Menu() {
    var uuid: UUID? = null
    override fun getTitle(player: Player?): String? {
        return translate(
            FlashMenuLanguage.GRANT_MENU_TITLE.string,
            "%PLAYER%",
            Flash.instance.cacheHandler.getUserCache().getName(
                uuid
            )
        )
    }

    override fun autoFill(): Boolean {
        return FlashMenuLanguage.GRANT_MENU_FILL.boolean
    }

    override fun getButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(GrantRankButton(uuid))
        buttons.add(GrantPermissionButton(uuid))
        return buttons
    }

    override fun getSize(player: Player): Int {
        return FlashMenuLanguage.GRANT_MENU_SIZE.int
    }

    @AllArgsConstructor
    private class GrantRankButton : Button() {
        var uuid: UUID? = null
        override fun getSlot(): Int {
            return FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_SLOT.int
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_MATERIAL.material)
                .setDurability(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_DATA.int)
                .setName(FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_NAME.string)
                .setLore(
                    FlashMenuLanguage.GRANT_MENU_RANK_BUTTON_LORE.stringList,
                    "%PLAYER%",
                    Flash.instance.cacheHandler.getUserCache().getName(uuid)
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            RanksMenu(uuid).openMenu((event.whoClicked as Player))
        }
    }

    @AllArgsConstructor
    private class GrantPermissionButton : Button() {
        var uuid: UUID? = null
        override fun getSlot(): Int {
            return FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_SLOT.int
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_MATERIAL.material)
                .setDurability(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_DATA.int)
                .setName(FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_NAME.string)
                .setLore(
                    FlashMenuLanguage.GRANT_MENU_PERMISSION_BUTTON_LORE.stringList,
                    "%PLAYER%",
                    Flash.instance.cacheHandler.getUserCache().getName(uuid)
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            player.closeInventory()
            GrantListener.Companion.grantPermissionTargetMap.put(player.name, PermissionBuild(uuid, null, null, null))
            player.sendMessage(
                CC.translate(
                    "&aNow, type the permission node you would like to grant to " + UserUtils.formattedName(
                        uuid
                    ) + "&a."
                )
            )
        }
    }

    private class RanksMenu(var uuid: UUID?) : PagedMenu<Rank?>() {
        init {
            objects = Flash.instance.rankHandler.sortedRanks
        }

        override fun getPageTitle(player: Player?): String? {
            return CC.translate(FlashMenuLanguage.GRANT_MENU_RANK_MENU_TITLE.string)
        }

        override fun getPageButtons(player: Player): List<Button> {
            val buttons: MutableList<Button> = ArrayList()
            val user: User = Flash.instance.userHandler.getUser(player.uniqueId, false)
            var i = 1
            for (rank in objects!!) {
                if (user.activeRank.getWeight() < rank.getWeight() && !player.isOp) continue
                buttons.add(RankButton(rank, i++, uuid))
            }
            return buttons
        }

        override fun getGlobalButtons(player: Player?): List<Button> {
            val buttons: MutableList<Button> = ArrayList()
            buttons.add(BackButton(5, GrantMenu(uuid)))
            return buttons
        }

        override fun autoFill(): Boolean {
            return true
        }
    }

    @AllArgsConstructor
    private class RankButton : Button() {
        var rank: Rank? = null
        override var slot = 0
        var target: UUID? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setDurability(ColorUtil.COLOR_MAP!![rank.getColor()]!!.woolData.toShort())
                .setName(
                    CC.applyRank(
                        CC.applyPlayer(
                            FlashMenuLanguage.GRANT_MENU_RANK_MENU_RANK_BUTTON_NAME.string,
                            target
                        ), rank
                    )
                )
                .setLore(
                    CC.applyRank(
                        CC.applyPlayer(
                            FlashMenuLanguage.GRANT_MENU_RANK_MENU_RANK_BUTTON_LORE.stringList,
                            target
                        ), rank
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            player.closeInventory()
            GrantListener.Companion.grantTargetMap.put(
                player.name,
                GrantBuild(target, rank.getUuid(), null, null, null)
            )
            player.sendMessage(
                CC.translate(
                    "&aNow, type the reason for granting the " + rank.getColoredName() + " &arank to " + UserUtils.formattedName(
                        target
                    ) + "&a."
                )
            )
        }
    }
}