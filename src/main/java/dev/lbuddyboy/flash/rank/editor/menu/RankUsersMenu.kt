package dev.lbuddyboy.flash.rank.editor.menu

import dev.lbuddyboy.flash.rank.Rankimport

dev.lbuddyboy.flash.user.Userimport dev.lbuddyboy.flash.user.menu.GrantsMenuimport dev.lbuddyboy.flash.util.bukkit.CCimport dev.lbuddyboy.flash.util.bukkit.CompatibleMaterialimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport dev.lbuddyboy.flash.util.menu.Buttonimport dev.lbuddyboy.flash.util.menu.Menuimport dev.lbuddyboy.flash.util.menu.button.BackButtonimport dev.lbuddyboy.flash.util.menu.paged.PagedMenuimport lombok.AllArgsConstructorimport org.bukkit.entity.Playerimport org.bukkit.event.inventory.InventoryClickEventimport org.bukkit.inventory.ItemStackimport java.util.*
class RankUsersMenu(var rank: Rank?) : PagedMenu<User?>() {
    init {
        objects = rank.getUsersWithRank()
    }

    override fun getPageTitle(player: Player?): String? {
        return "Inheritance Editor"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (user in objects!!) {
            if (user.activeRank.getUuid().toString() != rank.getUuid().toString()) continue
            buttons.add(UserButton(i++, user, this))
        }
        return buttons
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(BackButton(5, RankEditorMenu(rank)))
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