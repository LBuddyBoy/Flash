package dev.lbuddyboy.flash.command.user.punishment

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.Punishment
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.user.packet.GlobalMessagePacket
import dev.lbuddyboy.flash.user.packet.PunishmentAddPacket
import dev.lbuddyboy.flash.user.packet.PunishmentSendPacket
import dev.lbuddyboy.flash.util.*
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("warn")
@CommandPermission("flash.command.warn")
object WarnCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun mute(
        sender: CommandSender,
        @Name("target") uuid: UUID,
        @Name("duration") @Default("perm") time: String,
        @Name("reason {-p}") reason: String
    ) {
        val isPub = reason.contains("-p")
        val senderUUID = if (sender is Player) sender.uniqueId else null
        var duration = JavaUtils.parse(time)
        if (time.equals("perm", ignoreCase = true)) duration = Long.MAX_VALUE
        if (duration <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."))
            return
        }
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (sender is Player) {
            val senderUser: User = Flash.instance.userHandler.tryUser(senderUUID, true)
            if (!sender.hasPermission("flash.punish.permanent")) {
                val max = JavaUtils.parse(FlashLanguage.PUNISHMENTS_DEFAULT_PUNISH_TIME.string)
                if (duration > max) {
                    sender.sendMessage(
                        CC.translate(
                            "&cYou do not have permission to punish longer than " + TimeUtils.formatLongIntoDetailedString(
                                max / 1000
                            ) + "."
                        )
                    )
                    return
                }
            }
            if (senderUser.activeRank.getWeight() < user.activeRank.getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.string))
                return
            }
        }
        if (user.hasActivePunishment(PunishmentType.WARN)) {
            sender.sendMessage(
                CC.translate(
                    FlashLanguage.PUNISHMENTS_ALREADY_PUNISHED.string,
                    "%FORMAT%",
                    PunishmentType.WARN.format
                )
            )
            return
        }
        val punishment = Punishment(
            UUID.randomUUID(),
            PunishmentType.WARN,
            uuid,
            senderUUID,
            System.currentTimeMillis(),
            duration,
            reason,
            FlashLanguage.SERVER_NAME.string,
            !isPub
        )
        user.getPunishments().add(punishment)
        user.save(true)
        PunishmentAddPacket(uuid, punishment).send()
        Bukkit.getScheduler().runTask(Flash.instance) { PunishmentSendPacket(punishment).send() }
        if (senderUUID != null) UserUtils.addPunishment(senderUUID, punishment)
        GlobalMessagePacket(uuid, "&c&lYou have just been warned. &e&lReason: " + punishment.sentFor).send()
    }
}