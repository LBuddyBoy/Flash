package dev.lbuddyboy.flash.command.essentials.message

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Player

@CommandAlias("togglemessages|togglepms|tpm|tpms|tmsg|tmsgs|tmessages")
@CommandPermission("flash.command.togglemessages")
object ToggleMessagesCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun message(sender: Player) {
        val senderUser: User = Flash.instance.userHandler.tryUser(sender.uniqueId, false)
        senderUser.getPlayerInfo().isPmsOn = !senderUser.getPlayerInfo().isPmsOn
        if (senderUser.getPlayerInfo().isPmsOn) {
            sender.sendMessage(CC.translate("&aYou can now be messaged!"))
            return
        }
        sender.sendMessage(CC.translate("&cYou can no longer be messaged!"))
    }
}