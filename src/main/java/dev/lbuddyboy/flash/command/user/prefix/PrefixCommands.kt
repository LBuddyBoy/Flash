package dev.lbuddyboy.flash.command.user.prefix

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.menu.PrefixesMenu
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.user.packet.PrefixesUpdatePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("prefix|tag")
object PrefixCommands : BaseCommand() {
    @Default
    fun menu(sender: Player?) {
        PrefixesMenu().openMenu(sender!!)
    }

    @Subcommand("create")
    @CommandPermission("flash.command.prefix.create")
    fun create(sender: CommandSender, @Name("name") @Single name: String) {
        val prefix = Prefix(name, "", 100)
        if (Flash.instance.userHandler.getPrefix(name) != null) {
            sender.sendMessage(CC.translate("&cThat prefix already exists."))
            return
        }
        sender.sendMessage(CC.translate("&aCreated a new prefix under the name: $name."))
        Flash.instance.userHandler.getPrefixes().add(prefix)
        prefix.save()
    }

    @Subcommand("delete")
    @CommandPermission("flash.command.prefix.delete")
    @CommandCompletion("@prefixes")
    fun delete(sender: CommandSender, @Name("prefix") prefix: Prefix) {
        prefix.delete()
        Flash.instance.userHandler.getPrefixes().remove(prefix)
        PrefixesUpdatePacket(Flash.instance.userHandler.getPrefixes()).send()
        sender.sendMessage(CC.translate("&aDeleted a prefix under the name: " + prefix.id + "."))
    }

    @Subcommand("setweight|weight")
    @CommandPermission("flash.command.prefix.setweight")
    @CommandCompletion("@prefixes")
    fun setWeight(sender: CommandSender, @Name("prefix") prefix: Prefix, @Name("weight") weight: Int) {
        prefix.weight = weight
        prefix.save()
        sender.sendMessage(CC.translate("&aUpdated the weight of a prefix under the name: " + prefix.id + "."))
    }

    @Subcommand("setprefix|prefix")
    @CommandPermission("flash.command.prefix.setprefix")
    @CommandCompletion("@prefixes")
    fun setPrefix(sender: CommandSender, @Name("prefix") prefix: Prefix, @Name("weight") prefixName: String?) {
        prefix.display = prefixName
        prefix.save()
        sender.sendMessage(CC.translate("&aUpdated the prefix of a prefix under the name: " + prefix.id + "."))
    }
}