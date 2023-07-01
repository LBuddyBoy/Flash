package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.GameMode
import org.bukkit.entity.Player

@CommandAlias("gmc")
@CommandPermission("flash.command.gamemode.creative")
object GMCCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun gmc(sender: Player, @Name("target") @Default("self") target: Player) {
        target.gameMode = GameMode.CREATIVE
        if (sender === target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &fCREATIVE"))
        } else {
            sender.sendMessage(CC.translate("&c" + target.name + "'s GameMode&7: &fCREATIVE"))
        }
    }
}