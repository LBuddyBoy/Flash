package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Player

@CommandAlias("more|moreitems|stack")
@CommandPermission("flash.command.more")
object MoreCommand : BaseCommand() {
    @Default
    fun more(sender: Player) {
        sender.itemInHand.amount = 64
        sender.sendMessage(CC.translate("&aSuccessfully set the item amount in your hand to 64."))
    }
}