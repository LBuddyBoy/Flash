package dev.lbuddyboy.flash.command.user

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

@CommandAlias("staffchat|sc")
@CommandPermission("flash.command.staffchat")
object StaffChatCommand : BaseCommand() {
    @Default
    fun def(sender: Player, @Name("message") @Default("none") message: String) {
        if (!sender.hasPermission("flash.staff")) return
        val user: User = Flash.instance.userHandler.tryUser(sender.uniqueId, false)
        if (!message.equals("none", ignoreCase = true)) {
            StaffMessagePacket("&9[Staff Chat] " + user.displayName + "&7: &f" + message).send()
            return
        }
        user.getStaffInfo().isStaffChat = !user.getStaffInfo().isStaffChat
        if (user.getStaffInfo().isStaffChat) {
            sender.sendMessage(CC.translate("&aYour staff chat is now on!"))
            sender.setMetadata("staffchat", FixedMetadataValue(Flash.instance, true))
            return
        }
        sender.removeMetadata("staffchat", Flash.instance)
        sender.sendMessage(CC.translate("&cYour staff chat is now off!"))
    }
}