package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.packet.ServerBroadcastPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import java.util.*

@CommandAlias("broadcast|bc|alert")
@CommandPermission("flash.command.broadcast")
object BroadcastCommand : BaseCommand() {
    @Default
    fun broadcast(
        sender: CommandSender?,
        @Name("servers|global") @Split server: Array<String?>,
        @Name("message") message: String
    ) {
        ServerBroadcastPacket(
            Arrays.asList(*server),
            CC.translate(FlashLanguage.ESSENTIALS_BROADCAST_PREFIX.string + message)
        ).send()
    }
}