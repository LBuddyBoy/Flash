package dev.lbuddyboy.flash.util.menu.paged

import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.Menu
import dev.lbuddyboy.flash.util.menu.button.NextPageButton
import dev.lbuddyboy.flash.util.menu.button.PreviousPageButton
import org.bukkit.entity.Player

abstract class PagedMenu<T> : Menu() {
    open val buttonSlots = intArrayOf(
        12, 13, 14, 15, 16,
        21, 22, 23, 24, 25,
        30, 31, 32, 33, 34
    )
    var objects: List<T>? = null
    var page = 1
    abstract fun getPageTitle(player: Player?): String?
    abstract fun getPageButtons(player: Player): List<Button>
    open fun getGlobalButtons(player: Player?): List<Button> {
        return ArrayList()
    }

    override fun getTitle(player: Player?): String? {
        return getPageTitle(player) + " (" + page + "/" + getMaxPages() + ")"
    }

    override fun getButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var index = 0
        for (i in page * getMaxPageButtons() - getMaxPageButtons() until page * getMaxPageButtons()) {
            try {
                if (objects!!.size <= i) continue
                buttons.add(Button.Companion.fromButton(buttonSlots[index++], getPageButtons(player)[i]))
            } catch (ignored: Exception) {
            }
        }
        buttons.addAll(getGlobalButtons(player))
        buttons.add(PreviousPageButton(this, previousButtonSlot))
        buttons.add(NextPageButton(this, nextPageButtonSlot))
        return buttons
    }

    open val previousButtonSlot: Int
        get() = 20
    open val nextPageButtonSlot: Int
        get() = 26

    override fun getSize(player: Player): Int {
        return 45
    }

    open fun getMaxPageButtons(): Int {
        return buttonSlots.size
    }

    fun getMaxPages(): Int {
        return objects!!.size / getMaxPageButtons() + 1
    }
}