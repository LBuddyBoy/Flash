package dev.lbuddyboy.flash.util.bukkit;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Punishment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserUtils {

    public static String formattedName(UUID uuid) {
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            return "&4Console";
        }

        return user.getColoredName();
    }

    public static String formattedName(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return "&4Console";
        }

        return formattedName(((Player) sender).getUniqueId());
    }

    public static void addPunishment(UUID uuid, Punishment punishment) {
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        user.getStaffInfo().getKnownPunishments().add(punishment);
    }

}
