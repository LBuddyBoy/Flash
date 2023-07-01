package dev.lbuddyboy.flash.command.user.punishment

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Punishment
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.user.packet.PunishmentAddPacket
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("kick")
@CommandPermission("flash.command.kick")
object KickCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun ban(sender: CommandSender, @Name("target") uuid: UUID?, @Name("reason {-p}") reason: String) {
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
        val punishment = Punishment(
            UUID.randomUUID(),
            PunishmentType.KICK,
            uuid,
            senderUUID,
            System.currentTimeMillis(),
            Long.MAX_VALUE,
            reason,
            FlashLanguage.SERVER_NAME.string,
            !isPub
        )
        user.getPunishments().add(punishment)
        user.save(true)
        PunishmentAddPacket(uuid, punishment).send()
        val target = Bukkit.getPlayer(uuid)
        if (target != null) Bukkit.getScheduler()
            .runTask(Flash.instance) { target.kickPlayer(punishment.format()) }
        Bukkit.getScheduler().runTask(Flash.instance) { PunishmentSendPacket(punishment).send() }
        if (senderUUID != null) UserUtils.addPunishment(senderUUID, punishment)
    }
}