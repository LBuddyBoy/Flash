package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Demotion;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.Promotion;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.user.packet.GlobalMessagePacket;
import dev.lbuddyboy.flash.user.packet.GrantAddPacket;
import dev.lbuddyboy.flash.user.packet.PermissionAddPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.JavaUtils;
import dev.lbuddyboy.flash.util.PagedItem;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@CommandAlias("user|profile")
@CommandPermission("flash.command.user")
public class UserCommand extends BaseCommand {

    @Default
    public static void def(CommandSender sender, @Name("page") @Default("1") int page) {
        PagedItem item = new PagedItem(COMMANDS, FlashLanguage.USER_COMMAND_HELP.getStringList(), 5);

        item.send(sender, page);

        sender.sendMessage(CC.CHAT_BAR);
    }

    @Subcommand("help")
    public static void help(CommandSender sender, @Name("page") @Default("1") int page) {
        def(sender, page);
    }

    @Subcommand("info")
    @CommandCompletion("@target")
    public static void info(CommandSender sender, @Name("target") UUID uuid) {
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        sender.sendMessage(GSONUtils.getGSON().toJson(user, GSONUtils.USER));

    }

    @Subcommand("grantperm|addperm|addpermission")
    @CommandCompletion("@target @permissions")
    public static void permissionAdd(CommandSender sender, @Name("user") UUID uuid, @Split @Name("permissions") String[] permissions, @Single @Name("duration") String duration, @Name("reason") String reason) {
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

        if (Arrays.asList(permissions).contains("*") && !(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cOnly console can grant * permissions."));
            return;
        }

        for (String permission : permissions) {
            UserPermission userPermission = new UserPermission(permission, time, System.currentTimeMillis(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason);

            user.getPermissions().add(userPermission);

            sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_PERMISSION_SENDER.getString(),
                    "%PLAYER_DISPLAY%", user.getDisplayName(),
                    "%PERMISSION%", userPermission.getNode(),
                    "%DURATION%", userPermission.getExpireString()));

            String message = CC.translate(FlashLanguage.GRANTED_USER_PERMISSION_TARGET.getString(),
                    "%PERMISSION%", userPermission.getNode(),
                    "%DURATION%", userPermission.getExpireString());

            new GlobalMessagePacket(uuid, message).send();

        }

        if (Bukkit.getPlayer(uuid) == null) {
            user.save(true);
        }
        user.updatePerms();
        new PermissionAddPacket(uuid, user.getPermissions()).send();

    }

    @Subcommand("addpromotion")
    @CommandCompletion("@target")
    public static void promotionAdd(CommandSender sender, @Name("user") UUID uuid, @Name("time-long") long time, @Single @Name("fromRank") String fromRank, @Single @Name("toRank") String toRank) {

        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        Promotion promotion = new Promotion(fromRank, toRank, time);

        user.getPromotions().add(promotion);
        user.save(true);

    }

    @Subcommand("setjoinedstaffteam")
    @CommandCompletion("@target")
    public static void setjoinedstaffteam(CommandSender sender, @Name("user") UUID uuid, @Name("time-long") long time) {

        if (time <= 0) {
            sender.sendMessage(CC.translate("&cInvalid duration."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        user.getStaffInfo().setJoinedStaffTeam(time);
        user.save(true);

    }

    @Subcommand("grantrank|addrank")
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

        if (sender instanceof Player) {
            User senderUser = Flash.getInstance().getUserHandler().tryUser(((Player) sender).getUniqueId(), true);

            if (senderUser.getActiveRank().getWeight() < rank.getWeight()) {
                sender.sendMessage(CC.translate("&cThat rank is too high for you to grant..."));
                return;
            }

            if (!sender.hasPermission("grant.staff") && rank.isStaff()) {
                sender.sendMessage(CC.translate("&cYou do not have clearance to grant staff ranks..."));
                return;
            }
        }

        Grant grant = new Grant(UUID.randomUUID(), rank.getUuid(), rank.getName(), (sender instanceof Player) ? ((Player) sender).getUniqueId() : null, reason, System.currentTimeMillis(), time, scopes);

        if (user.getActiveRank().isStaff() && user.getActiveRank().getWeight() < grant.getRank().getWeight()) {
            Promotion promotion = new Promotion(user.getActiveRank().getColoredName(), grant.getRank().getColoredName(), System.currentTimeMillis());
            user.getPromotions().add(promotion);
        }

        if (grant.getRank().isStaff() && !user.getActiveRank().isStaff()) {
            user.getStaffInfo().setJoinedStaffTeam(System.currentTimeMillis());
        }

        user.getGrants().add(grant);
        if (Bukkit.getPlayer(uuid) == null) {
            user.save(true);
        }
        user.updateGrants();

        new GrantAddPacket(uuid, grant).send();

        sender.sendMessage(CC.translate(FlashLanguage.GRANTED_USER_RANK_SENDER.getString(),
                "%PLAYER_DISPLAY%", user.getDisplayName(),
                "%RANK%", rank.getDisplayName(),
                "%DURATION%", grant.getExpireString()));

        String message = CC.translate(FlashLanguage.GRANTED_USER_RANK_TARGET.getString(),
                "%RANK%", rank.getDisplayName(),
                "%DURATION%", grant.getExpireString());

        new GlobalMessagePacket(uuid, message).send();


    }

    private static final int itemsPerPage = 5;
    private static final List<String> COMMANDS = Arrays.asList(
            "&c/user editor &7- &fdisplay a menu to edit all users",
            "&c/user editor <user> &7- &fdisplay a menu to edit a specific user",
            "&c/user info <target> &7- &fdisplay a list of the users attributes",
            "&c/grants <target> &7- &fdisplay a list of the users grants (ranks & perms)",
            "&c/user grantrank <user> <rank> <time> <servers> <reason> &7- &fapplies a rank grant to an user",
            "&c/user grantperm <user> <permission> <duration> <reason> &7- &fadds a permission to a user"
    );

}
