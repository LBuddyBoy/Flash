package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("sudo")
@CommandPermission("flash.command.sudo")
object SudoCommand : BaseCommand() {
    @Default
    fun sudo(
        sender: CommandSender,
        @Name("player") @Default("other") target: Player,
        @Name("command|message") command: String
    ) {
        target.chat(command)
        sender.sendMessage(CC.translate("&aSuccessfully made " + target.name + " do " + command + "."))
    }
}