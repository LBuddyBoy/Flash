package dev.lbuddyboy.flash.rank.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("rank|ranks")
public class RankCommand extends BaseCommand {

    @Subcommand("list|dump")
    public static void list(CommandSender sender) {
        for (String s : FlashLanguage.RANK_LIST_HEADER.getStringList()) sender.sendMessage(CC.translate(s));

        for (Rank rank : Flash.getInstance().getRankHandler().getRanks().values()) {
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

    @Subcommand("create")
    @CommandPermission("flash.command.rank.create")
    public static void create(CommandSender sender, @Name("rank") String name) {
        Rank rank = Flash.getInstance().getRankHandler().createRank(name);

        rank.setDisplayName(name);
        rank.save(true);

        Flash.getInstance().getRankHandler().getRanks().put(rank.getUuid(), rank);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_CREATE.getString(), "%rank%", rank.getColoredName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("delete")
    @CommandPermission("flash.command.rank.delete")
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
    public static void toggleDefault(CommandSender sender, @Name("rank") @Flags("rank") Rank rank) {
        String previous = rank.isDefaultRank() ? "True" : "False";

        rank.setDefaultRank();
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_DEFAULT.getString(), "%rank%", rank.getColoredName(), "%old-status%", previous, "%new-status%", rank.isDefaultRank() ? "&aTrue" : "&cFalse"));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setname|name|rename")
    @CommandPermission("flash.command.rank.setname")
    public static void setName(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("newName") String newName) {
        String previous = rank.getColoredName();

        rank.setName(newName);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_NAME.getString(), "%rank%", previous, "%new-rank%", rank.getColoredName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setdisplayname|displayname|setdisplay")
    @CommandPermission("flash.command.rank.setdisplayname")
    public static void setDisplayName(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("newDisplay") String newDisplayName) {
        String previous = rank.getDisplayName();

        rank.setDisplayName(newDisplayName);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_DISPLAY_NAME.getString(), "%rank%", rank.getColoredName(), "%old-display%", previous, "%new-display%", rank.getDisplayName()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setcolor|color|setdisplaycolor")
    @CommandPermission("flash.command.rank.setcolor")
    public static void setColor(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("newColor") String color) {
        String previous = rank.getColor() + rank.getColor().name();

        ChatColor chatColor = null;
        try {
            chatColor = ChatColor.valueOf(color);
        } catch (Exception ignored) {
            sender.sendMessage(CC.translate("&cThat is not that valid color."));
        }
        
        rank.setColor(chatColor);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_COLOR.getString(), "%rank%", rank.getColoredName(), "%old-color%", previous, "%new-color%", rank.getColor() + rank.getColor().name()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setweight|weight|setpriority")
    @CommandPermission("flash.command.rank.setweight")
    public static void setWeight(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("newWeight") int weight) {
        int previous = rank.getWeight();
        
        rank.setWeight(weight);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_WEIGHT.getString(), "%rank%", rank.getColoredName(), "%old-weight%", previous, "%new-weight%", rank.getWeight()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setprefix|prefix")
    @CommandPermission("flash.command.rank.setprefix")
    public static void setPrefix(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("prefix") String prefix) {
        String previous = rank.getPrefix();
        
        rank.setPrefix(prefix);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_PREFIX.getString(), "%rank%", rank.getColoredName(), "%old-prefix%", previous, "%new-prefix%", rank.getPrefix()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("setsuffix|suffix")
    @CommandPermission("flash.command.rank.setsuffix")
    public static void setSuffix(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("prefix") String suffix) {
        String previous = rank.getPrefix();
        
        rank.setSuffix(suffix);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_SET_SUFFIX.getString(), "%rank%", rank.getColoredName(), "%old-suffix%", previous, "%new-suffix%", rank.getSuffix()));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("addpermission|addperm")
    @CommandPermission("flash.command.rank.addpermission")
    public static void addPermission(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("permission") String permission) {

        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank already has that permission."));
            return;
        }

        rank.getPermissions().add(permission);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_ADD_PERM.getString(), "%rank%", rank.getColoredName(), "%permission%", permission));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

    @Subcommand("removepermission|removeperm")
    @CommandPermission("flash.command.rank.removepermission")
    public static void removePermission(CommandSender sender, @Name("rank") @Flags("rank") Rank rank, @Name("permission") String permission) {

        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat rank does not have that permission."));
            return;
        }

        rank.getPermissions().remove(permission);
        rank.save(true);

        sender.sendMessage(CC.translate(FlashLanguage.RANK_REMOVE_PERM.getString(), "%rank%", rank.getColoredName(), "%permission%", permission));

        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();

    }

}
