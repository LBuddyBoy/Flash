package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import dev.lbuddyboy.flash.user.packet.PunishmentAddPacket;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@CommandAlias("staffchat|sc")
@CommandPermission("flash.command.staffchat")
public class StaffChatCommand extends BaseCommand {

    @Default
    public static void def(Player sender, @Name("message") @Default("none") String message) {
        if (!sender.hasPermission("flash.staff")) return;
        User user = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        if (!message.equalsIgnoreCase("none")) {
            new StaffMessagePacket("&9[Staff Chat] " + user.getDisplayName() + "&7: &f" + message).send();
            return;
        }

        user.getStaffInfo().setStaffChat(!user.getStaffInfo().isStaffChat());

        if (user.getStaffInfo().isStaffChat()) {
            sender.sendMessage(CC.translate("&aYour staff chat is now on!"));
            sender.setMetadata("staffchat", new FixedMetadataValue(Flash.getInstance(), true));
            return;
        }

        sender.removeMetadata("staffchat", Flash.getInstance());
        sender.sendMessage(CC.translate("&cYour staff chat is now off!"));

    }

}
