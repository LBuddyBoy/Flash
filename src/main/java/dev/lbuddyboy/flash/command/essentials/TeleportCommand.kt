package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player

@CommandAlias("teleport|tp")
@CommandPermission("flash.command.teleport")
object TeleportCommand : BaseCommand() {
    @Default
    fun teleport(sender: Player, @Name("target") @Default("other") @Flags("other") target: Player?) {
        sender.teleport(target)
    }
}