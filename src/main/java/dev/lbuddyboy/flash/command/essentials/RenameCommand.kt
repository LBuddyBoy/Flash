package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.util.bukkit.ItemUtils
import org.bukkit.entity.Player

@CommandAlias("rename")
@CommandPermission("flash.command.rename")
object RenameCommand : BaseCommand() {
    @Default
    fun rename(sender: Player, @Name("name") name: String?) {
        ItemUtils.setDisplayName(sender.itemInHand, name)
    }
}