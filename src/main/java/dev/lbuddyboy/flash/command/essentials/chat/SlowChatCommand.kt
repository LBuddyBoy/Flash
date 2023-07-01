package dev.lbuddyboy.flash.command.essentials.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender

@CommandAlias("slowchat|chatslow|delaychat|chatdelay")
@CommandPermission("flash.command.clearchat")
object SlowChatCommand : BaseCommand() {
    @Default
    fun def(sender: CommandSender, @Name("seconds") seconds: Int) {
        sender.sendMessage(CC.translate("&aChat's message delay is now $seconds seconds."))
        FlashLanguage.CHAT_SLOW.update(seconds)
        Flash.instance.chatHandler.setSlowChat(seconds)
    }
}