package dev.lbuddyboy.flash.server.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.server.model.Notification
import dev.lbuddyboy.flash.server.packet.NotificationsUpdatePacket
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class NotificationsMenu(var filter: String, var target: User) : PagedMenu<Notification?>() {
    init {
        objects =
            if (filter == "all") Flash.instance.serverHandler.getNotifications() else if (filter == "read") Flash.instance.serverHandler.getReadNotifications(
                target
            ) else Flash.instance.serverHandler.getUnReadNotifications(target)
    }

    override fun getPageTitle(player: Player?): String? {
        return "Notifications"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (notification in objects!!) {
            buttons.add(NotificationButton(i++, notification, player, this))
        }
        return buttons
    }

    override fun update(player: Player) {
        val inventory = player.openInventory.topInventory
        for (slot in buttonSlots) {
            inventory.setItem(slot - 1, null)
        }
        super.update(player)
    }

    override fun getGlobalButtons(player: Player?): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        if (objects!!.isEmpty()) {
            buttons.add(EmptyButton(23))
        }
        buttons.add(object : Button() {
            override fun getSlot(): Int {
                return 5
            }

            override fun clickUpdate(): Boolean {
                return true
            }

            override fun getItem(): ItemStack? {
                return ItemBuilder(CompatibleMaterial.getMaterial("SIGN"))
                    .setName("&eChange Filter")
                    .setLore(
                        Arrays.asList(
                            CC.MENU_BAR,
                            (if (filter == "all") "&a&l✓" else "&c&l✕") + " &7All Notifications",
                            (if (filter == "read") "&a&l✓" else "&c&l✕") + " &7All Read Notifications",
                            (if (filter == "unread") "&a&l✓" else "&c&l✕") + " &7All Unread Notifications",
                            "",
                            "&7Click to scroll thru the filters",
                            CC.MENU_BAR
                        )
                    )
                    .create()
            }

            override fun action(event: InventoryClickEvent) {
                if (event.whoClicked !is Player) return
                val player = event.whoClicked as Player
                val user: User = Flash.instance.userHandler.tryUser(player.uniqueId, false)
                filter = if (filter == "all") "read" else if (filter == "read") "unread" else "all"
                objects =
                    if (filter == "all") Flash.instance.serverHandler.getNotifications() else if (filter == "read") Flash.instance.serverHandler.getReadNotifications(
                        user
                    ) else Flash.instance.serverHandler.getUnReadNotifications(user)
                update(player)
            }
        })
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
        var player: Player? = null
        var menu: NotificationsMenu? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun clickUpdate(): Boolean {
            return true
        }

        override fun getItem(): ItemStack? {
            val user: User = Flash.instance.userHandler.tryUser(
                player!!.uniqueId, false
            )
            return if (user.getPlayerInfo().readNotifications.contains(notification.getId())) {
                ItemBuilder(Material.BOOK_AND_QUILL)
                    .setName(CC.DARK_RED + notification.getTitle())
                    .setLore(
                        Arrays.asList(
                            CC.MENU_BAR,
                            "&gMessage&7: &f" + notification.getMessage(),
                            "&gSent At&7: &f" + notification!!.sentAtDate,
                            CC.MENU_BAR
                        )
                    )
                    .create()
            } else ItemBuilder(Material.BOOK_AND_QUILL)
                .setName(CC.DARK_RED + notification.getTitle())
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&gMessage&7: &f" + notification.getMessage(),
                        "&gSent At&7: &f" + notification!!.sentAtDate,
                        "",
                        "&7Click to mark this notification as read.",
                        CC.MENU_BAR
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            val user: User = Flash.instance.userHandler.tryUser(player.uniqueId, false)
            if (user.getPlayerInfo().readNotifications.contains(notification.getId())) {
                player.sendMessage(CC.translate("&aYou already have that notification marked as read."))
                return
            }
            user.getPlayerInfo().readNotifications.add(notification.getId())
            user.save(true)
            NotificationsUpdatePacket(Flash.instance.serverHandler.getNotifications()).send()
            player.sendMessage(CC.translate("&aYou have marked that notification as read."))
            menu!!.update(player)
            menu!!.objects =
                if (menu!!.filter == "all") Flash.instance.serverHandler.getNotifications() else if (menu!!.filter == "read") Flash.instance.serverHandler.getReadNotifications(
                    user
                ) else Flash.instance.serverHandler.getUnReadNotifications(user)
        }
    }
}