package dev.lbuddyboy.flash.command.transport

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.bukkit.*
import org.bukkit.command.CommandSender

@CommandAlias("transportdata|transport|transfer")
@CommandPermission("flash.command.transport")
object TransportDataCommand : BaseCommand() {
    @Default
    fun help(sender: CommandSender?) {
    }

    @CommandAlias("luckpermsranks|lpranks")
    fun luckpermsranks(sender: CommandSender?) {
        Tasks.runAsync { Flash.instance.transportHandler.getLuckPermsTransport().transportRanks(sender) }
    }

    @CommandAlias("luckpermsusers|lpusers")
    fun luckperms(sender: CommandSender?) {
        Tasks.runAsync { Flash.instance.transportHandler.getLuckPermsTransport().transportUsers(sender) }
    }
}