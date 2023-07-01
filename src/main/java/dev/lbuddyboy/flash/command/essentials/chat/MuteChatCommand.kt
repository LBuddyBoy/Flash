package dev.lbuddyboy.flash.command.essentials.chat

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender

@CommandAlias("mutechat|chatmute|silencechat|togglechat")
@CommandPermission("flash.command.clearchat")
object MuteChatCommand : BaseCommand() {
    @Default
    fun def(sender: CommandSender) {
        FlashLanguage.CHAT_MUTED.update(!FlashLanguage.CHAT_MUTED.boolean)
        Flash.instance.chatHandler.setChatMuted(FlashLanguage.CHAT_MUTED.boolean)
        sender.sendMessage(CC.translate("&aChat is now " + if (FlashLanguage.CHAT_MUTED.boolean) "muted" else "un-muted"))
    }
}