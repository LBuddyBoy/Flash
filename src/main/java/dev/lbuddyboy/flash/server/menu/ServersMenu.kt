package dev.lbuddyboy.flash.server.menu

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.server.Server
import dev.lbuddyboy.flash.server.listener.ServerListener
import dev.lbuddyboy.flash.server.packet.ServerStopPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.CompatibleMaterial
import dev.lbuddyboy.flash.util.bukkit.ItemBuilder
import dev.lbuddyboy.flash.util.menu.Button
import dev.lbuddyboy.flash.util.menu.paged.PagedMenu
import lombok.AllArgsConstructor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class ServersMenu : PagedMenu<Server?>() {
    init {
        objects = ArrayList<Any?>(Flash.instance.serverHandler.getServers().values())
    }

    override fun getPageTitle(player: Player?): String? {
        return "Server List"
    }

    override fun getPageButtons(player: Player): List<Button> {
        val buttons: MutableList<Button> = ArrayList()
        var i = 0
        for (server in objects!!) {
            buttons.add(ServerButton(i++, server))
        }
        return buttons
    }

    override fun autoUpdate(): Boolean {
        return true
    }

    @AllArgsConstructor
    private class ServerButton : Button() {
        override var slot = 0
        var server: Server? = null
        override fun getSlot(): Int {
            return slot
        }

        override fun clickUpdate(): Boolean {
            return true
        }

        override fun getItem(): ItemStack? {
            return ItemBuilder(
                if (server!!.isOffline) CompatibleMaterial.getMaterial("BARRIER") else CompatibleMaterial.getMaterial(
                    "BEACON"
                )
            )
                .setName("&g&l" + server.getName())
                .setLore(
                    Arrays.asList(
                        CC.MENU_BAR,
                        "&hStatus&7: &r" + server!!.status(),
                        "&hOnline&7: &f" + server.getOnline() + "/" + server.getMaxPlayers(),
                        "&hMOTD&7: &f" + server.getMotd(),
                        "",
                        "&7Left Click to shut down the server",
                        "&7Right Click to run a server specific command.",
                        CC.MENU_BAR
                    )
                )
                .create()
        }

        override fun action(event: InventoryClickEvent) {
            if (event.whoClicked !is Player) return
            val player = event.whoClicked as Player
            if (event.click == ClickType.LEFT) {
                ServerStopPacket(server.getName()).send()
                return
            }
            player.sendMessage(CC.translate("&aType the command you would like to run on that server."))
            player.closeInventory()
            ServerListener.Companion.serverCommandMap.put(player.name, server)
        }
    }
}