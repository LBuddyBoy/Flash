package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player

@CommandAlias("teleporthere|tphere|s|tph")
@CommandPermission("flash.command.teleport.here")
object TeleportHereCommand : BaseCommand() {
    @Default
    fun teleport(sender: Player?, @Name("target") @Default("other") @Flags("other") target: Player) {
        target.teleport(sender)
    }
}