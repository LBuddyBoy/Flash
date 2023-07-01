package dev.lbuddyboy.flash.command.server

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.server.menu.ServersMenu
import org.bukkit.entity.Player

@CommandAlias("servers|serverlist")
@CommandPermission("flash.command.servers")
object ServersCommand : BaseCommand() {
    @Default
    fun def(sender: Player?) {
        ServersMenu().openMenu(sender!!)
    }
}