package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.*
import org.bukkit.entity.Player

@CommandAlias("teleportpos|tppos|location|loc|relocate")
@CommandPermission("flash.command.teleport.position")
object TeleportPosCommand : BaseCommand() {
    @Default
    fun teleport(
        sender: Player,
        @Name("target") @Default("self") target: Player,
        @Name("x") x: Int,
        @Name("y") y: Int,
        @Name("z") z: Int
    ) {
        val location = Location(target.world, x.toDouble(), y.toDouble(), z.toDouble())
        location.x = location.blockX + 0.5
        location.z = location.blockZ + 0.5
        sender.teleport(location)
    }
}