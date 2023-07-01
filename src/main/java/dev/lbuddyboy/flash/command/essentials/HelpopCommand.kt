package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.packet.StaffMessageListPacket
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Player

@CommandAlias("helpop|request")
@CommandPermission("flash.command.helpop")
object HelpopCommand : BaseCommand() {
    @Default
    fun helpop(sender: Player, @Name("message") reason: String?) {
        val user: User = Flash.instance.userHandler.tryUser(sender.uniqueId, false)
        if (user.getPlayerInfo().lastRequestSent > System.currentTimeMillis()) {
            sender.sendMessage(
                CC.translate(
                    "&cYou cannot do this for another " + TimeUtils.formatLongIntoDetailedString(
                        (user.getPlayerInfo().lastRequestSent - System.currentTimeMillis()) / 1000
                    )
                )
            )
            return
        }
        StaffMessageListPacket(
            CC.applyPlayer(
                FlashLanguage.ESSENTIALS_HELPOP_STAFF_MESSAGE.stringList,
                sender.uniqueId
            ), arrayOf<Any?>("%SERVER%", FlashLanguage.SERVER_NAME.string, "%REASON%", reason)
        ).send()
        sender.sendMessage(CC.translate(FlashLanguage.ESSENTIALS_HELPOP_SENDER_MESSAGE.string))
        user.getPlayerInfo().lastRequestSent = System.currentTimeMillis() + 60000L
    }
}