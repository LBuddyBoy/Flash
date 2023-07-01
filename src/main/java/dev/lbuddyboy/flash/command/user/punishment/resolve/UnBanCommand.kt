package dev.lbuddyboy.flash.command.user.punishment.resolve

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.user.packet.PunishmentRemovePacket
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("unban|unbanish|pardon")
@CommandPermission("flash.command.unban")
object UnBanCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun unban(sender: CommandSender, @Name("target") uuid: UUID?, @Name("reason {-p}") reason: String) {
        val isPub = reason.contains("-p")
        val senderUUID = if (sender is Player) sender.uniqueId else null
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (sender is Player) {
            val senderUser: User = Flash.instance.userHandler.tryUser(senderUUID, true)
            if (senderUser.activeRank.getWeight() < user.activeRank.getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.string))
                return
            }
        }
        val punishment = user.getActivePunishment(PunishmentType.BAN)
        if (punishment == null) {
            sender.sendMessage(
                CC.translate(
                    FlashLanguage.PUNISHMENTS_NOT_PUNISHED.string,
                    "%FORMAT%",
                    PunishmentType.BAN.format
                )
            )
            return
        }
        punishment.isRemoved = true
        punishment.removedBy = senderUUID
        punishment.removedAt = System.currentTimeMillis()
        punishment.isRemovedSilent = !isPub
        punishment.removedFor = reason
        PunishmentRemovePacket(uuid, punishment).send()
        user.save(true)
        PunishmentSendPacket(punishment).send()
    }
}