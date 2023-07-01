package dev.lbuddyboy.flash.command.essentials.message

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("block|ignore")
@CommandPermission("flash.command.ignore")
object BlockCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun message(sender: CommandSender, @Name("target") targetUUID: UUID?) {
        val senderPlayer = sender as Player
        val senderUUID = senderPlayer.uniqueId
        val senderUser: User = Flash.instance.userHandler.tryUser(senderUUID, false)
        if (senderUser.getBlocked().contains(senderUUID)) {
            sender.sendMessage(CC.translate("&cThat player is already blocked."))
            return
        }
        senderUser.getBlocked().add(targetUUID)
        sender.sendMessage(CC.translate("&aSuccessfully blocked " + UserUtils.formattedName(targetUUID) + "&a."))
    }
}