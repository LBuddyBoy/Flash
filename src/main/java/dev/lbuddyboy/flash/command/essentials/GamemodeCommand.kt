package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.GameMode
import org.bukkit.entity.Player

@CommandAlias("gamemode|gm")
@CommandPermission("flash.command.gamemode")
object GamemodeCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target @gamemodes")
    fun rename(sender: Player, @Name("target") @Default("other") target: Player, @Name("name") gameMode: GameMode) {
        target.gameMode = gameMode
        if (sender === target) {
            sender.sendMessage(CC.translate("&cGameMode&7: &f" + gameMode.name))
        } else {
            sender.sendMessage(CC.translate("&c" + target.name + "'s GameMode&7: &f" + gameMode.name))
        }
    }
}