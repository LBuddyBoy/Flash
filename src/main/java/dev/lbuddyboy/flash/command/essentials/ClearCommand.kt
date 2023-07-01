package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("clear|ci|clearinv|clearinventory")
@CommandPermission("flash.command.clear")
object ClearCommand : BaseCommand() {
    @Default
    fun clear(sender: CommandSender, @Name("target") @Default("self") target: Player) {
        target.inventory.clear()
        target.updateInventory()
        sender.sendMessage(CC.translate("&aSuccessfully cleared inventory."))
    }
}