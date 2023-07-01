package dev.lbuddyboy.flash.rank.editor.menu

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.rank.editor.listener.RankEditorListenerimport dev.lbuddyboy.flash.util.bukkit.CCimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport dev.lbuddyboy.flash.util.menu.Buttonimport dev.lbuddyboy.flash.util.menu.button.BackButtonimport dev.lbuddyboy.flash.util.menu.paged.PagedMenuimport lombok.AllArgsConstructorimport org.bukkit.Materialimport org.bukkit.entity.Playerimport org.bukkit.event.inventory.ClickTypeimport org.bukkit.event.inventory.InventoryClickEventimport org.bukkit.inventory.ItemStackimport java.util.*import java.util.stream.Collectors

class PermissionEditorMenu(var rank: Rank) : PagedMenu<String?>() {
    init {
        objects = rank.getPermissions()
    }

    override fun getPageTitle(player: Player?): String? {
        return "Permission Editor"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (permission in objects!!) {
            buttons.add(PermissionButton(i++, rank, permission))
        }
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(BackButton(4, RankEditorMenu(rank)))
        buttons.add(AddPermissionButton(6, rank))
        return buttons
    }

    @AllArgsConstructor
    private class PermissionButton : Button() {
        override var slot = 0
        var rank: Rank? = null
        var permission: String? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.PAPER)
                .setName("&c$permission")
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&fClick to remove this permission from the " + rank.getColoredName() + "&f rank.",
                        CC.MENU_BAR
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            rank.getPermissions().remove(permission)
            rank!!.save(true)
            player.sendMessage(CC.translate("&cRemoved the " + permission + " permission from the " + rank.getColoredName() + "&c rank."))
        }
    }

    private class PermissionAddMenu(var rank: Rank?) : PagedMenu<String?>() {
        init {
            objects = Flash.instance.commandHandler.getKnownPermissionsMap().keySet().stream()
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList())
        }

        override fun getPageTitle(player: Player?): String? {
            return "Add Permission"
        }

        override fun getButtonSlots(): IntArray {
            return intArrayOf(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
            )
        }

        override fun getSize(player: Player): Int {
            return 54
        }

        override fun autoFill(): Boolean {
            return true
        }

        override fun autoUpdate(): Boolean {
            return true
        }

        override fun getMaxPageButtons(): Int {
            return buttonSlots.size
        }

        override fun getPreviousButtonSlot(): Int {
            return 1
        }

        override fun getNextPageButtonSlot(): Int {
            return 9
        }

        override fun getPageButtons(player: Player): List<Button> {
            val buttons: MutableList<Button> = ArrayList()
            var i = 0
            for (permission in objects!!) {
                if (rank.getPermissions().contains(permission)) continue
                buttons.add(PermissionAddButton(i++, permission, rank))
            }
            return buttons
        }
    }

    @AllArgsConstructor
    private class PermissionAddButton : Button() {
        override var slot = 0
        var permission: String? = null
        var rank: Rank? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.PAPER)
                .setName("&c$permission")
                .setLore("&fClick to add the &a" + permission + "&f to the " + rank.getColoredName() + "&f rank.")
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            rank.getPermissions().add(permission)
            rank!!.save(true)
        }
    }

    @AllArgsConstructor
    private class AddPermissionButton : Button() {
        override var slot = 0
        var rank: Rank? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setName("&aAdd a Permission")
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&fLeft click to add a custom permission to the " + rank.getColoredName() + "&f rank.",
                        "&fRight click to view all the permissions editor.",
                        CC.MENU_BAR
                    )
                )
                .setDurability(5)
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            if (event.click == ClickType.RIGHT) {
                player.closeInventory()
                PermissionAddMenu(rank).openMenu(player)
                return
            }
            player.closeInventory()
            RankEditorListener.Companion.rankPermissionEditMap.put(player, rank)
            player.sendMessage(CC.translate("&aType the permission you would like to add to the " + rank.getColoredName() + "&a rank."))
        }
    }
}