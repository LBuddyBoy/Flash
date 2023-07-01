package dev.lbuddyboy.flash.command.user.punishment

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.user.menu.staffhistory.StaffHistorySelectionMenu
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("staffhistory|staffhist|sh")
@CommandPermission("flash.command.staffhistory")
object StaffHistoryCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun def(sender: Player?, @Name("target") target: UUID?) {
        StaffHistorySelectionMenu(target).openMenu(sender!!)
    }
}