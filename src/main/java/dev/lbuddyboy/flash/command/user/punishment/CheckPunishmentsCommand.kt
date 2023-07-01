package dev.lbuddyboy.flash.command.user.punishment

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.user.menu.PunishmentHistorySelectionMenu
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("check|c|checkpunishments|punishments|history")
@CommandPermission("flash.command.history")
object CheckPunishmentsCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun ban(sender: Player?, @Name("target") uuid: UUID?) {
        PunishmentHistorySelectionMenu(uuid).openMenu(sender!!)
    }
}