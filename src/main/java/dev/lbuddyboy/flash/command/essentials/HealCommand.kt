package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("heal")
@CommandPermission("flash.command.heal")
object HealCommand : BaseCommand() {
    @Default
    fun heal(sender: CommandSender, @Name("target") @Default("self") target: Player) {
        target.health = 20.0
        target.foodLevel = 20
        target.saturation = 20f
        sender.sendMessage(CC.translate("&aSuccessfully healed."))
    }
}