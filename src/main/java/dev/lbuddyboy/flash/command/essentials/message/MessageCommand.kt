package dev.lbuddyboy.flash.command.essentials.message

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("message|msg|m|dm|pm|privatemessage|privatemsg")
@CommandPermission("flash.command.message")
object MessageCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun message(sender: CommandSender, @Name("target") targetUUID: UUID?, @Name("message") message: String) {
        if (sender !is Player) {
            val targetUser: User = Flash.instance.userHandler.tryUser(targetUUID, false)
            val targetPlayer = Bukkit.getPlayer(targetUUID)
            targetPlayer.sendMessage(CC.translate("&7(From &4&lCONSOLE&7): $message"))
            sender.sendMessage(CC.translate("&7(To " + targetUser.coloredName + "&7): " + message))
            targetUser.getPlayerInfo().reply = null
            return
        }
        val senderPlayer = sender
        val senderUUID = senderPlayer.uniqueId
        val senderUser: User = Flash.instance.userHandler.tryUser(senderUUID, false)
        val targetUser = Flash.instance.userHandler.tryUser(targetUUID, false)
        if (targetUser == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (targetUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cThat player currently has you blocked."))
            return
        }
        if (senderUser.getBlocked().contains(senderUUID) && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cYou currently have that player blocked."))
            return
        }
        if (!senderUser.getPlayerInfo().isPmsOn) {
            sender.sendMessage(CC.translate("&cYour messages are currently toggled off."))
            return
        }
        if (!targetUser.getPlayerInfo().isPmsOn && !senderPlayer.hasPermission("flash.message.bypass")) {
            sender.sendMessage(CC.translate("&cThat players messages are currently toggled off."))
            return
        }
        val targetPlayer = Bukkit.getPlayer(targetUUID)
        targetPlayer.sendMessage(CC.translate("&7(From " + senderUser.coloredName + "&7): " + message))
        senderPlayer.sendMessage(CC.translate("&7(To " + targetUser.coloredName + "&7): " + message))
        senderUser.getPlayerInfo().reply = targetUUID
        targetUser.getPlayerInfo().reply = senderUUID
    }
}