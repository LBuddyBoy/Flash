package dev.lbuddyboy.flash.command.user.prefix;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.menu.PrefixesMenu;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.user.packet.PrefixesUpdatePacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("prefix|tag")
public class PrefixCommands extends BaseCommand {

    @Default
    public static void menu(Player sender) {
        new PrefixesMenu().openMenu(sender);
    }

    @Subcommand("create")
    @CommandPermission("flash.command.prefix.create")
    public static void create(CommandSender sender, @Name("name") @Single String name) {


        Prefix prefix = new Prefix(name, "", 100);

        if (Flash.getInstance().getUserHandler().getPrefix(name) != null) {
            sender.sendMessage(CC.translate("&cThat prefix already exists."));
            return;
        }

        sender.sendMessage(CC.translate("&aCreated a new prefix under the name: " + name + "."));
        Flash.getInstance().getUserHandler().getPrefixes().add(prefix);
        prefix.save();

    }

    @Subcommand("delete")
    @CommandPermission("flash.command.prefix.delete")
    public static void delete(CommandSender sender, @Name("prefix") Prefix prefix, @Name("name") @Single String name) {

        sender.sendMessage(CC.translate("&aDeleted a prefix under the name: " + name + "."));
        Flash.getInstance().getUserHandler().getPrefixes().remove(prefix);
        new PrefixesUpdatePacket(Flash.getInstance().getUserHandler().getPrefixes()).send();

    }

    @Subcommand("setweight|weight")
    @CommandPermission("flash.command.prefix.setweight")
    @CommandCompletion("@prefixes")
    public static void setWeight(CommandSender sender, @Name("prefix") Prefix prefix, @Name("weight") int weight) {
        prefix.setWeight(weight);
        prefix.save();

        sender.sendMessage(CC.translate("&aUpdated the weight of a prefix under the name: " + prefix.getId() + "."));
    }

    @Subcommand("setprefix|prefix")
    @CommandPermission("flash.command.prefix.setprefix")
    @CommandCompletion("@prefixes")
    public static void setPrefix(CommandSender sender, @Name("prefix") Prefix prefix, @Name("weight") String prefixName) {
        prefix.setDisplay(prefixName);
        prefix.save();

        sender.sendMessage(CC.translate("&aUpdated the prefix of a prefix under the name: " + prefix.getId() + "."));
    }

}
