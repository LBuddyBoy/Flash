package dev.lbuddyboy.flash.command.essentials.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@CommandAlias("clearchat|cc")
@CommandPermission("flash.command.clearchat")
object ClearChatCommand : BaseCommand() {
    @Default
    fun def(sender: CommandSender?) {
        for (i in 0..299) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("flash.staff")) continue
                player.sendMessage(" ")
            }
        }
        Bukkit.broadcastMessage(CC.translate("&dThe chat has just been cleared by " + UserUtils.formattedName(sender) + "."))
    }
}