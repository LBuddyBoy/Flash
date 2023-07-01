package dev.lbuddyboy.flash.server.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.server.listener.NotificationListener
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class NotificationsEditorMenu(var target: UUID) : PagedMenu<Notification?>() {
    init {
        objects = Flash.instance.serverHandler.getNotifications()
    }

    override fun getPageTitle(player: Player?): String? {
        return "Notifications Editor"
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (notification in objects!!) {
            buttons.add(NotificationButton(i++, notification))
        }
        return buttons
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        if (objects!!.isEmpty()) {
            buttons.add(EmptyButton(23))
        }
        buttons.add(AddNotificationButton())
        return buttons
    }

    @AllArgsConstructor
    private class EmptyButton : Button() {
        override var slot = 0
        override fun getSlot(): Int {
            return slot
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("BARRIER")).setName("&cCouldn't find any notifications...")
                .create()
        }
    }

    @AllArgsConstructor
    private class NotificationButton : Button() {
        override var slot = 0
        var notification: Notification? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun clickUpdate(): Boolean {
            return true
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(Material.BOOK_AND_QUILL)
                .setName(CC.DARK_RED + notification.getTitle())
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&4Message&7: &f" + notification.getMessage(),
                        "&4Sent At&7: &f" + notification!!.sentAtDate,
                        "",
                        "&7Click to &cdelete&7 this notification.",
                        CC.MENU_BAR
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            notification!!.delete()
            NotificationsUpdatePacket(Flash.instance.serverHandler.getNotifications()).send()
            player.sendMessage(CC.translate("&aYou have marked that notification as read."))
        }
    }

    @AllArgsConstructor
    private class AddNotificationButton : Button() {
        override fun getSlot(): Int {
            return 5
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(CompatibleMaterial.getMaterial("STAINED_CLAY"))
                .setDurability(DyeColor.GREEN.woolData.toShort())
                .setName("&aCreate Notification &7(Click)")
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            player.closeInventory()
            NotificationListener.Companion.notificationsAdd.add(player.name)
            player.sendMessage(CC.translate("&aType the title of the notification you would like to create."))
        }
    }
}