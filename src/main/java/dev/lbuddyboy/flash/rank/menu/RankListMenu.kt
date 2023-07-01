package dev.lbuddyboy.flash.rank.menu

import dev.lbuddyboy.flash.Flashimport

dev.lbuddyboy.flash.rank.Rankimport dev.lbuddyboy.flash.util.Callbackimport dev.lbuddyboy.flash.util.bukkit.ColorUtilimport dev.lbuddyboy.flash.util.bukkit.ItemBuilderimport dev.lbuddyboy.flash.util.menu.Buttonimport dev.lbuddyboy.flash.util.menu.paged.PagedMenuimport lombok.AllArgsConstructorimport org.bukkit.Materialimport org.bukkit.entity.Playerimport org.bukkit.event.inventory.InventoryClickEventimport org.bukkit.inventory.ItemStack
class RankListMenu(var callback: Callback<Player, Rank>) : PagedMenu<Rank?>() {
    init {
        objects = Flash.instance.rankHandler.sortedRanks
    }

    override fun getPageTitle(player: Player?): String? {
        return "Select a rank..."
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        val slot = 0
        for (rank in objects!!) {
            buttons.add(RankButton(slot, callback, rank))
        }
        return buttons
    }

    @AllArgsConstructor
    private class RankButton : Button() {
        override var slot = 0
            get() = slot
        var callback: Callback<Player, Rank>? = null
        var rank: Rank? = null
        override val item: ItemStack?
            get() = ItemBuilder(Material.WOOL)
                .setName(rank!!.getDisplayName())
                .setDurability(ColorUtil.COLOR_MAP!![rank.getColor()]!!.woolData.toShort())
                .create()

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            callback!!.call(player, rank!!)
        }
    }
}