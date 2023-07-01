package dev.lbuddyboy.flash.command.essentials.message

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("unblock|unignore")
@CommandPermission("flash.command.unignore")
object UnBlockCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun message(sender: Player, @Name("target") targetUUID: UUID?) {
        val senderUUID = sender.uniqueId
        val senderUser: User = Flash.instance.userHandler.tryUser(senderUUID, false)
        if (!senderUser.getBlocked().contains(senderUUID)) {
            sender.sendMessage(CC.translate("&cThat player is not blocked."))
            return
        }
        senderUser.getBlocked().remove(targetUUID)
        sender.sendMessage(CC.translate("&aSuccessfully unblocked " + UserUtils.formattedName(targetUUID) + "&a."))
    }
}