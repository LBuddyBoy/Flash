package dev.lbuddyboy.flash.rank.editor.menu

import dev.lbuddyboy.flash.rank.Rankimport

dev.lbuddyboy.flash.rank.editor.EditorTypeimport dev.lbuddyboy.flash.rank.editor.RankEditimport dev.lbuddyboy.flash.rank.editor.listener.RankEditorListenerimport dev.lbuddyboy.flash.rank.menu.RankListMenuimport dev.lbuddyboy.flash.util.*import dev.lbuddyboy.flash.util.bukkit.CCimport

dev.lbuddyboy.flash.util.bukkit.ColorUtilimport dev.lbuddyboy.flash.util.bukkit.CompatibleMaterialimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport dev.lbuddyboy.flash.util.menu.Buttonimport dev.lbuddyboy.flash.util.menu.Menuimport dev.lbuddyboy.flash.util.menu.button.BackButtonimport lombok.AllArgsConstructorimport org.bukkit.Materialimport org.bukkit.entity.Playerimport org.bukkit.event.inventory.InventoryClickEventimport org.bukkit.inventory.ItemStackimport java.util.*
@AllArgsConstructor
class RankEditorMenu : Menu() {
    var rank: Rank? = null
    override fun getTitle(player: Player?): String? {
        return "Editing: " + rank.getName()
    }

    override fun getButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(BackButton(1, RankListMenu { p: Player, r: Rank? ->
            p.closeInventory()
            RankEditorMenu(r).openMenu(p)
        }))
        buttons.add(RankDisplayButton(rank))
        buttons.add(UsersButton(rank))
        for (type in EditorType.values()) {
            buttons.add(RankActionButton(type.slot, rank, type.button, type, type.stack))
        }
        return buttons
    }

    override fun getSize(player: Player): Int {
        return 54
    }

    override fun autoFill(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class RankActionButton : Button() {
        override var slot = 0
        var rank: Rank? = null
        var callback: Callback<Player, Rank>? = null
        var type: EditorType? = null
        var stack: ItemStack? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return stack
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            callback!!.call(player, rank!!)
            if (type == EditorType.PERMISSIONS || type == EditorType.INHERITANCE) return
            RankEditorListener.Companion.rankEditorMap.put(player, RankEdit(rank, type))
        }
    }

    @AllArgsConstructor
    private class RankDisplayButton : Button() {
        var rank: Rank? = null
        override fun getSlot(): Int {
            return 13
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setName(rank.getColoredName())
                .setDurability(ColorUtil.COLOR_MAP!![rank.getColor()]!!.woolData.toShort())
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&cDisplay Name&7: &f" + rank!!.getDisplayName(),
                        "&cWeight&7: &f" + rank.getWeight(),
                        "&cColor&7: &f" + rank.getColor().name,
                        "&cPrefix&7: &f" + rank.getPrefix(),
                        "&cSuffix&7: &f" + rank.getSuffix(),
                        "&cDefault Rank&7: &f" + if (rank.isDefaultRank()) "&a&l✓" else "&c&l✕",
                        CC.MENU_BAR
                    )
                )
                .create()
        }
    }

    @AllArgsConstructor
    private class UsersButton : Button() {
        var rank: Rank? = null
        override fun getSlot(): Int {
            return 15
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("SKULL_ITEM"))
                .setName("&eUsers")
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&7Click to view all users with this rank.",
                        CC.MENU_BAR
                    )
                )
                .setDurability(3)
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            RankUsersMenu(rank).openMenu(player)
        }
    }
}