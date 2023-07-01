package dev.lbuddyboy.flash.command.user.grant

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.menu.GrantMenu
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("grant")
@CommandPermission("flash.command.grant")
object GrantCommand : BaseCommand() {
    @Default
    @CommandCompletion("@target")
    fun grant(sender: Player, @Name("target") uuid: UUID?) {
        if (Flash.instance.userHandler.tryUser(uuid, true) == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.string))
            return
        }
        GrantMenu(uuid).openMenu(sender)
    }
}