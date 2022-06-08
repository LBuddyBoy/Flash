package dev.lbuddyboy.flash.command.rank;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.editor.menu.RankEditorMenu;
import dev.lbuddyboy.flash.rank.menu.RankListMenu;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.PagedItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandAlias("rank|ranks")
@CommandPermission("flash.command.rank")
public class RankCommand extends BaseCommand {

    @Default
    public static void def(CommandSender sender, @Name("page") @Default("1") int page) {
        PagedItem item = new PagedItem(COMMANDS, FlashLanguage.RANK_HELP.getStringList(), 5);

        item.send(sender, page);

        sender.sendMessage(CC.CHAT_BAR);
    }

    @Subcommand("help")
    public static void help(CommandSender sender, @Name("page") @Default("1") int page) {
        def(sender, page);
    }

    @Subcommand("list|dump")
    public static void list(CommandSender sender) {
        for (String s : FlashLanguage.RANK_LIST_HEADER.getStringList()) sender.sendMessage(CC.translate(s));

        for (Rank rank : Flash.getInstance().getRankHandler().getSortedRanks()) {
            sender.sendMessage(CC.translate(FlashLanguage.RANK_LIST_FORMAT.getString(),
                    "%rank%", rank.getName(),
                    "%display%", rank.getDisplayName(),
                    "%default%", rank.isDefaultRank() ? "True" : "False",
                    "%weight%", rank.getWeight(),
                    "%prefix%", rank.getPrefix(),
                    "%suffix%", rank.getSuffix()
            ));
        }

    }

    @Subcommand("info")
    @CommandPermission("flash.command.rank.info")
    @CommandCompletion("@rank")
    public static void info(CommandSender sender, @Name("rank") Rank rank) {
        sender.sendMessage(CC.translate("&cPermissions: " + StringUtils.join(rank.getPermissions(), ", ")));
        sender.sendMessage(CC.translate("&cInherited Permissions: " + StringUtils.join(rank.getInheritedPermissions(), ", ")));
        sender.sendMessage(CC.translate("&cInherited Ranks: " + StringUtils.join(rank.getInheritance(), ", ")));
    }

    @Subcommand("create")
    @CommandPermission("flash.command.rank.create")
    public static void create(CommandSender sender, @Name("rank") String name) {
        Rank rank = Flash.getInstance().getRankHandler().getRank(name);

        if (rank != null) {
            sender.sendMessage(CC.translate(FlashLanguage.RANK_EXISTS.getString()));
            return;
        }
        rank = Flash.getInstance().getRankHandler().createRank(name);

        rank.setDisplayName(name);
        rank.save(true);

        Flash.getInstance().getRankHandler().getRanks().put(rank.getUuid(), rank);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_CREATE.getString(), "%rank%", rank.getColoredName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("delete")
    @CommandPermission("flash.command.rank.delete")
    @CommandCompletion("@rank")
    public static void delete(CommandSender sender, @Name("rank") Rank rank) {
        if (FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("YAML") || FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("FLATFILE")) {
            sender.sendMessage(CC.translate("&cRanks cannot be deleted due to the cache type you are using. Delete it manually in the ranks.yml."));
            return;
        }

        rank.delete();

        sender.sendMessage(CC.translate(FlashLanguage.RANK_DELETE.getString(), "%rank%", rank.getColoredName()));

    }

    @Subcommand("toggledefault")
    @CommandPermission("flash.command.rank.toggledefault")
    @CommandCompletion("@rank")
    public static void toggleDefault(CommandSender sender, @Name("rank") Rank rank) {
        String previous = rank.isDefaultRank() ? "True" : "False";

        rank.setDefaultRank(!rank.isDefaultRank());
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_DEFAULT.getString(), "%rank%", rank.getColoredName(), "%old-status%", previous, "%new-status%", rank.isDefaultRank() ? "&aTrue" : "&cFalse"));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setname|name|rename")
    @CommandPermission("flash.command.rank.setname")
    @CommandCompletion("@rank")
    public static void setName(CommandSender sender, @Name("rank") Rank rank, @Name("newName") String newName) {
        String previous = rank.getColoredName();

        rank.setName(newName);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_NAME.getString(), "%rank%", previous, "%new-rank%", rank.getColoredName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setdisplayname|displayname|setdisplay")
    @CommandPermission("flash.command.rank.setdisplayname")
    @CommandCompletion("@rank")
    public static void setDisplayName(CommandSender sender, @Name("rank") Rank rank, @Name("newDisplay") String newDisplayName) {
        String previous = rank.getDisplayName();

        rank.setDisplayName(newDisplayName);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_DISPLAY_NAME.getString(), "%rank%", rank.getColoredName(), "%old-display%", previous, "%new-display%", rank.getDisplayName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("editor")
    @CommandPermission("flash.command.rank.editor")
    public static void editor(Player sender) {
        new RankListMenu(((player, rank) -> {
            new RankEditorMenu(rank).openMenu(player);
        })).openMenu(sender);
    }

    @Subcommand("setcolor|color|setdisplaycolor")
    @CommandPermission("flash.command.rank.setcolor")
    @CommandCompletion("@rank @chatcolors")
    public static void setColor(CommandSender sender, @Name("rank") Rank rank, @Name("newColor") ChatColor color) {
        String previous = rank.getColor() + rank.getColor().name();

        rank.setColor(color);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_COLOR.getString(), "%rank%", rank.getColoredName(), "%old-color%", previous, "%new-color%", rank.getColor() + rank.getColor().name()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setweight|weight|setpriority")
    @CommandPermission("flash.command.rank.setweight")
    @CommandCompletion("@rank")
    public static void setWeight(CommandSender sender, @Name("rank") Rank rank, @Name("newWeight") int weight) {
        int previous = rank.getWeight();

        rank.setWeight(weight);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_WEIGHT.getString(), "%rank%", rank.getColoredName(), "%old-weight%", previous, "%new-weight%", rank.getWeight()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setprefix|prefix")
    @CommandPermission("flash.command.rank.setprefix")
    @CommandCompletion("@rank")
    public static void setPrefix(CommandSender sender, @Name("rank") Rank rank, @Name("prefix") String prefix) {
        String previous = rank.getPrefix();

        rank.setPrefix(prefix);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_PREFIX.getString(), "%rank%", rank.getColoredName(), "%old-prefix%", previous, "%new-prefix%", rank.getPrefix()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setsuffix|suffix")
    @CommandPermission("flash.command.rank.setsuffix")
    @CommandCompletion("@rank")
    public static void setSuffix(CommandSender sender, @Name("rank") Rank rank, @Name("prefix") String suffix) {
        String previous = rank.getPrefix();

        rank.setSuffix(suffix);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_SUFFIX.getString(), "%rank%", rank.getColoredName(), "%old-suffix%", previous, "%new-suffix%", rank.getSuffix()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("addpermission|addperm")
    @CommandPermission("flash.command.rank.addpermission")
    @CommandCompletion("@rank")
    public static void addPermission(CommandSender sender, @Name("rank") Rank rank, @Name("permission") String permission) {

        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank already has that permission."));
            return;
        }

        rank.getPermissions().add(permission);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_ADD_PERM.getString(), "%rank%", rank.getColoredName(), "%permission%", permission));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("removepermission|removeperm|delperm|delpermission")
    @CommandPermission("flash.command.rank.removepermission")
    @CommandCompletion("@rank")
    public static void removePermission(CommandSender sender, @Name("rank") Rank rank, @Name("permission") String permission) {

        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank does not have that permission."));
            return;
        }

        rank.getPermissions().remove(permission);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_REMOVE_PERM.getString(), "%rank%", rank.getColoredName(), "%permission%", permission));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("addinheritance|addinherit|addparent")
    @CommandPermission("flash.command.rank.addinheritance")
    @CommandCompletion("@rank @rank")
    public static void addInherit(CommandSender sender, @Name("rank") Rank rank, @Name("rank") String inherit) {

        if (rank.getInheritance().contains(inherit)) {
            sender.sendMessage(CC.translate("&cThat rank already has that inheritance."));
            return;
        }

        rank.getInheritance().add(inherit);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_ADD_INHERIT.getString(), "%rank%", rank.getColoredName(), "%inherit%", inherit));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("removeinheritance|reminherit|reminheritance|removeinherit|delinherit|delinheritance")
    @CommandPermission("flash.command.rank.removepermission")
    @CommandCompletion("@rank @rank")
    public static void removeInherit(CommandSender sender, @Name("rank") Rank rank, @Name("rank") String inherit) {

        if (!rank.getInheritance().contains(inherit)) {
            sender.sendMessage(CC.translate("&cThat rank doesn't have that inheritance."));
            return;
        }

        rank.getInheritance().remove(inherit);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_REMOVE_INHERIT.getString(), "%rank%", rank.getColoredName(), "%inherit%", inherit));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    private static final int itemsPerPage = 5;
    private static final List<String> COMMANDS = Arrays.asList(
            "&c/rank editor &7- &fdisplay a menu to edit all ranks",
            "&c/rank info <rank> &7- &fdisplay a list of the ranks attributes",
            "&c/rank create <name> &7- &fcreates a new rank",
            "&c/rank delete <rank> &7- &fdeletes an existing rank",
            "&c/rank setname <rank> <name> &7- &fsets the ranks name attribute",
            "&c/rank setprefix <rank> <prefix> &7- &fsets the ranks prefix attribute",
            "&c/rank setsuffix <rank> <suffix> &7- &fsets the ranks suffix attribute",
            "&c/rank addpermission <rank> <permission> &7- &fadds a permission to a rank",
            "&c/rank delpermission <rank> <permission> &7- &fdeletes a permission from a rank",
            "&c/rank addinheritance <rank> <rank> &7- &fadds an inheritance to a rank",
            "&c/rank delinheritance <rank> <rank> &7- &fdeletes an inheritance from a rank",
            "&c/rank setweight <rank> <weight> &7- &fsets the ranks weight attribute",
            "&c/rank setdisplayname <rank> <name> &7- &fsets the ranks display name attribute",
            "&c/rank setcolor <rank> <color> &7- &fsets the ranks color attribute"
    );

}
