package dev.lbuddyboy.flash.command.essentials

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket
import dev.lbuddyboy.flash.util.bukkit.CC
import dev.lbuddyboy.flash.util.bukkit.UserUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

@CommandAlias("freeze|ss|screenshare")
@CommandPermission("flash.command.freeze")
object FreezeCommand : BaseCommand() {
    @Default
    fun freeze(sender: CommandSender?, @Name("target") @Flags("other") target: Player) {
        if (sender is Player) {
            if (sender.hasMetadata("frozen")) return
        }
        if (target.hasMetadata("frozen")) {
            target.removeMetadata("frozen", Flash.instance)
            target.sendMessage(CC.translate("&7"))
            target.sendMessage(CC.translate("&b&lFREEZE!"))
            target.sendMessage(CC.translate("&aYou have just been unfrozen. Have a great day."))
            target.sendMessage(CC.translate("&7"))
        } else {
            target.setMetadata("frozen", FixedMetadataValue(Flash.instance, true))
            target.sendMessage(CC.translate("&7"))
            target.sendMessage(CC.translate("&b&lFREEZE!"))
            target.sendMessage(CC.translate("&cYou have just been frozen. Watch out for a staff message and follow their orders, or you'll be punished."))
            target.sendMessage(CC.translate("&4&lDO NOT LOG OUT. DO NOT LOG OUT. DO NOT LOG OUT."))
            target.sendMessage(CC.translate("&7"))
        }
        StaffMessagePacket(
            "&4&l[Freeze] " + target.displayName + " &fhas just been &b" + (if (target.hasMetadata("frozen")) "frozen" else "unfrozen") + " &fby &4" + UserUtils.formattedName(
                sender
            )
        ).send()
    }
}