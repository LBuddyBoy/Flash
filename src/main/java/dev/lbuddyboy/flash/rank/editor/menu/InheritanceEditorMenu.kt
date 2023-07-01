package dev.lbuddyboy.flash.rank.editor.menu

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.rank.editor.listener.RankEditorListenerimport dev.lbuddyboy.flash.util.bukkit.CCimport dev.lbuddyboy.flash.util.bukkit.ColorUtilimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport dev.lbuddyboy.flash.util.menu.Buttonimport dev.lbuddyboy.flash.util.menu.button.BackButtonimport dev.lbuddyboy.flash.util.menu.paged.PagedMenuimport lombok.AllArgsConstructorimport org.bukkit.Materialimport org.bukkit.entity.Playerimport org.bukkit.event.inventory.InventoryClickEventimport org.bukkit.inventory.ItemStackimport java.util.*
class InheritanceEditorMenu(var rank: Rank) : PagedMenu<String?>() {
    init {
        objects = rank.getInheritance()
    }

    override fun getPageTitle(player: Player?): String? {
        return "Inheritance Editor"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (rankName in objects!!) {
            val inheritance = Flash.instance.rankHandler.getRank(rankName) ?: continue
            buttons.add(InheritanceButton(i++, rank, inheritance))
        }
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(BackButton(4, RankEditorMenu(rank)))
        buttons.add(AddInheritanceButton(6, rank))
        return buttons
    }

    @AllArgsConstructor
    private class InheritanceButton : Button() {
        override var slot = 0
        var rank: Rank? = null
        var inherited: Rank? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setName(inherited.getColoredName())
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&fClick to remove this inheritance from the " + rank.getColoredName() + "&f rank.",
                        CC.MENU_BAR
                    )
                )
                .setDurability(ColorUtil.COLOR_MAP!![inherited.getColor()]!!.woolData.toShort())
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            rank.getInheritance().remove(inherited.getName())
            rank!!.save(true)
            player.sendMessage(CC.translate("&cRemoved the " + inherited.getColoredName() + "&c inheritance from the " + rank.getColoredName() + "&c rank."))
        }
    }

    @AllArgsConstructor
    private class AddInheritanceButton : Button() {
        override var slot = 0
        var rank: Rank? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.WOOL)
                .setName("&aAdd an Inheritance")
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&fClick to add a new inheritance to the " + rank.getColoredName() + "&f rank.",
                        CC.MENU_BAR
                    )
                )
                .setDurability(5)
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            player.closeInventory()
            RankEditorListener.Companion.rankInheritanceEditMap.put(player, rank)
            player.sendMessage(CC.translate("&aType the rank you would like the " + rank.getColoredName() + "&a rank to inherit."))
        }
    }
}