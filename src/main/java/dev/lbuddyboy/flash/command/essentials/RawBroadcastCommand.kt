package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.user.packet.ServerBroadcastPacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import java.util.*

@CommandAlias("rawbroadcast|rawbc|rawalert")
@CommandPermission("flash.command.rawbc")
object RawBroadcastCommand : BaseCommand() {
    @Default
    fun broadcast(
        sender: CommandSender?,
        @Name("servers|global") @Split server: Array<String?>,
        @Name("message") message: String?
    ) {
        ServerBroadcastPacket(Arrays.asList(*server), CC.translate(message)).send()
    }
}