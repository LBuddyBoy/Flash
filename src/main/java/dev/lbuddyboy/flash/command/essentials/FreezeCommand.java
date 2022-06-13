package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.bukkit.UserUtils;
import net.luckperms.api.event.util.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@CommandAlias("freeze|ss|screenshare")
@CommandPermission("flash.command.freeze")
public class FreezeCommand extends BaseCommand {

    @Default
    public static void freeze(CommandSender sender, @Name("target") @Flags("other") Player target) {
        if (target.hasMetadata("frozen")) {
            target.removeMetadata("frozen", Flash.getInstance());

            target.sendMessage(CC.translate("&7"));
            target.sendMessage(CC.translate("&b&lFREEZE!"));
            target.sendMessage(CC.translate("&aYou have just been unfrozen. Have a great day."));
            target.sendMessage(CC.translate("&7"));
        } else {
            target.setMetadata("frozen", new FixedMetadataValue(Flash.getInstance(), true));

            target.sendMessage(CC.translate("&7"));
            target.sendMessage(CC.translate("&b&lFREEZE!"));
            target.sendMessage(CC.translate("&cYou have just been frozen. Watch out for a staff message and follow their orders, or you'll be punished."));
            target.sendMessage(CC.translate("&7"));
        }

        new StaffMessagePacket("&4&l[Freeze] " + target.getDisplayName() + " &fhas just been &b" + (target.hasMetadata("frozen") ? "frozen" : "unfrozen") + " &fby &4" + UserUtils.formattedName(sender)).send();
    }

}
