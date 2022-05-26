package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.GlobalMessagePacket;
import dev.lbuddyboy.flash.user.packet.GrantAddPacket;
import dev.lbuddyboy.flash.user.packet.PermissionAddPacket;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("user|profile")
public class UserCommand extends BaseCommand {

    @Default
    public static void help(CommandSender sender) {

    }

    @Subcommand("info")
    @CommandCompletion("@target")
    public static void info(CommandSender sender, @Name("target") UUID uuid) {
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }


    }

    @Subcommand("addperm|addpermission")
    @CommandCompletion("@target")
    public static void permissionAdd(CommandSender sender, @Name("user") UUID uuid, @Single @Name("permission") String permission, @Single @Name("duration") String duration, @Name("reason") String reason) {
        long time = JavaUtils.parse(duration);
        if (duration.equalsIgnoreCase("perm")) time = Long.MAX_VALUE;

        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        UserPermission userPermission = new UserPermission(permission, time, System.currentTimeMillis(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason);

        if (Bukkit.getPlayer(uuid) != null) {
            user.getPermissions().add(userPermission);
            user.save(true);
            user.updatePerms();
        } else {
            new PermissionAddPacket(uuid, userPermission).send();
        }

        sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_PERMISSION_SENDER.getString(),
                "%PLAYER_DISPLAY%", user.getDisplayName(),
                "%PERMISSION%", userPermission.getNode(),
                "%DURATION%", userPermission.getExpireString()));

        String message = CC.translate(FlashLanguage.GRANTED_USER_PERMISSION_TARGET.getString(),
                "%PERMISSION%", userPermission.getNode(),
                "%DURATION%", userPermission.getExpireString());

        new GlobalMessagePacket(uuid, message).send();

    }

    @Subcommand("addrank|grant")
    @CommandCompletion("@target @rank")
    public static void rankAdd(CommandSender sender, @Name("user") UUID uuid, @Single @Name("rank") Rank rank, @Name("duration") @Single String duration, @Name("scopes") @Split String[] scopes, @Name("reason") String reason) {
        long time = JavaUtils.parse(duration);
        if (duration.equalsIgnoreCase("perm")) time = Long.MAX_VALUE;

        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        Grant grant = new Grant(UUID.randomUUID(), rank.getUuid(), rank.getName(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason, System.currentTimeMillis(), time, scopes);

        if (Bukkit.getPlayer(uuid) != null) {
            user.getGrants().add(grant);
            user.save(true);
            user.updateGrants();
        } else {
            new GrantAddPacket(uuid, grant).send();
        }

        sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_RANK_SENDER.getString(),
                "%PLAYER_DISPLAY%", user.getDisplayName(),
                "%RANK%", rank.getDisplayName(),
                "%DURATION%", grant.getExpireString()));

        String message = CC.translate(FlashLanguage.GRANTED_USER_RANK_TARGET.getString(),
                "%RANK%", rank.getDisplayName(),
                "%DURATION%", grant.getExpireString());

        new GlobalMessagePacket(uuid, message).send();

    }

}
