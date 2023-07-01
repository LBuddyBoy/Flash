package dev.lbuddyboy.flash.command.transport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import org.bukkit.command.CommandSender;

@CommandAlias("transportdata|transport|transfer")
@CommandPermission("flash.command.transport")
public class TransportDataCommand extends BaseCommand {

    @Default
    public static void help(CommandSender sender) {

    }

    @CommandAlias("luckpermsranks|lpranks")
    public static void luckpermsranks(CommandSender sender) {
        Tasks.runAsync(() -> Flash.getInstance().getTransportHandler().getLuckPermsTransport().transportRanks(sender));
    }

    @CommandAlias("luckpermsusers|lpusers")
    public static void luckperms(CommandSender sender) {
        Tasks.runAsync(() -> {
            Flash.getInstance().getTransportHandler().getLuckPermsTransport().transportUsers(sender);
        });
    }

}
