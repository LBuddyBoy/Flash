package dev.lbuddyboy.flash.user.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.impl.RedisUser;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.UserUpdatePacket;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("user|profile")
public class UserCommand extends BaseCommand {

    @Subcommand("info")
    public static void info(CommandSender sender, @Name("user") UUID uuid) {
        User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        sender.sendMessage(CC.translate(user.getUuid().toString()));
        sender.sendMessage(CC.translate(user.getName()));
        sender.sendMessage(CC.translate(user.getIp()));
        sender.sendMessage(CC.translate(user.getActiveRank().getColoredName()));
        if (user instanceof RedisUser) {
            sender.sendMessage(CC.translate(((RedisUser) user).toJson()));
        }
    }

    @Subcommand("addperm|addpermission")
    public static void permissionAdd(CommandSender sender, @Name("user") UUID uuid, @Single @Name("permission") String permission, @Single @Name("duration") String duration, @Name("reason") String reason) {
        long time = JavaUtils.parse(duration);
        if (duration.equalsIgnoreCase("perm")) time = Long.MAX_VALUE;

        if (time <= 0) {
            return;
        }

        User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        UserPermission userPermission = new UserPermission(permission, time, System.currentTimeMillis(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason);

        user.getPermissions().add(userPermission);
        user.save(true);

        new UserUpdatePacket(uuid, user).send();

        sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_PERMISSION.getString(),
                "%PLAYER_DISPLAY%", user.getDisplayName(),
                "%PERMISSION%", userPermission.getNode(),
                "%DURATION%", userPermission.getExpireString()));

    }

    @Subcommand("addrank|grant")
    public static void rankAdd(CommandSender sender, @Name("user") UUID uuid, @Single @Name("rank") Rank rank, @Name("duration") @Single String duration, @Name("scopes") @Split String[] scopes, @Name("reason") String reason) {
        long time = JavaUtils.parse(duration);
        if (duration.equalsIgnoreCase("perm")) time = Long.MAX_VALUE;

        if (time <= 0) {
            return;
        }

        User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        Grant grant = new Grant(UUID.randomUUID(), rank.getUuid(), rank.getName(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason, System.currentTimeMillis(), time, scopes);

        user.getGrants().add(grant);
        user.save(true);
        user.updateGrants();

        new UserUpdatePacket(uuid, user).send();

        sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_RANK.getString(),
                "%PLAYER_DISPLAY%", user.getDisplayName(),
                "%RANK%", rank.getDisplayName(),
                "%DURATION%", grant.getExpireString()));

    }

}
