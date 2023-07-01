package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("feed|eat|saturate")
@CommandPermission("flash.command.feed")
object FeedCommand : BaseCommand() {
    @Default
    fun feed(sender: CommandSender, @Name("target") @Default("self") target: Player) {
        target.foodLevel = 20
        sender.sendMessage(CC.translate("&aSuccessfully fed."))
    }
}