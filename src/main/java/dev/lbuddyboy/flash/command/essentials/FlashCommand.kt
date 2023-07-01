package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import java.io.IOException

@CommandAlias("flash|core")
@CommandPermission("flash.command.core")
object FlashCommand : BaseCommand() {
    @HelpCommand
    fun help(sender: CommandSender, help: CommandHelp) {
        sender.sendMessage(CC.translate("&4&lFlash Help"))
        help.showHelp()
    }

    @Subcommand("reload|reloadconfig")
    fun reload(sender: CommandSender) {
        Flash.instance.reloadConfig()
        try {
            Flash.instance.menusYML.reloadConfig()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        sender.sendMessage(CC.translate("&cSuccessfully reloaded the menus.yml & config.yml"))
    }
}