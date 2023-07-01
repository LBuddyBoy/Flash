package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.GameMode
import org.bukkit.entity.Player

@CommandAlias("gms")
@CommandPermission("flash.command.gamemode.survival")
object GMSCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun gms(sender: Player, @Name("target") @Default("self") target: Player) {
        target.gameMode = GameMode.SURVIVAL
        if (sender === target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &fSURVIVAL"))
        } else {
            sender.sendMessage(CC.translate("&c" + target.name + "'s GameMode&7: &fSURVIVAL"))
        }
    }
}