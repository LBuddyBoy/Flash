package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("world|switchworld|changeworld")
@CommandPermission("flash.command.world")
object WorldCommand : BaseCommand() {
    @Default
    @CommandCompletion("@worlds")
    fun feed(sender: CommandSender, @Name("target") @Default("self") target: Player, @Name("world") world: String?) {
        val bukkitWorld = Bukkit.getWorld(world)
        if (bukkitWorld == null) {
            sender.sendMessage(CC.translate("&cThat world does not exist."))
            return
        }
        target.teleport(Location(bukkitWorld, target.location.x, target.location.y, target.location.z))
    }
}