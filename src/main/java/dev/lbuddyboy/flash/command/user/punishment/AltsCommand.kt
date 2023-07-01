package dev.lbuddyboy.flash.command.user.punishment

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.user.model.PunishmentType
import dev.lbuddyboy.flash.util.bukkit.CC
import org.apache.commons.lang.StringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors

@CommandAlias("alts|dupeip")
@CommandPermission("flash.command.alts")
object AltsCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun alts(sender: CommandSender, @Name("target") uuid: UUID?) {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        if (sender is Player) {
            val senderUser: User = Flash.instance.userHandler.tryUser(
                sender.uniqueId, true
            )
            if (senderUser.activeRank.getWeight() < user.activeRank.getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.string))
                return
            }
        }
        val alts = Flash.instance.userHandler.relativeAlts(user.getIp())
        val coloredAlts = alts.stream().map { alt: UUID? -> Flash.instance.userHandler.tryUser(alt, true) }
            .map { obj: User -> obj.colorAlt() }.collect(Collectors.toList())
        val translate = StringUtils.join(
            Arrays.stream(PunishmentType.values()).map { type: PunishmentType -> type.color.toString() + type.plural }
                .collect(Collectors.toList()), "&7 - ")
        sender.sendMessage(CC.translate("" + user.coloredName + "'s &4Alts:"))
        sender.sendMessage(CC.translate("&7Offline &7- $translate"))
        sender.sendMessage(
            CC.translate(
                "&7> " + if (alts.size <= 0) "&cNone" else StringUtils.join(
                    coloredAlts,
                    "&7, "
                )
            )
        )
    }
}