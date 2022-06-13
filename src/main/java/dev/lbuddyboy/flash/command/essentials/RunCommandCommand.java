package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.user.packet.ServerCommandPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@CommandAlias("runcommand|executecommand")
@CommandPermission("flash.command.runcommand")
public class RunCommandCommand extends BaseCommand {

    @Default
    public static void broadcast(CommandSender sender, @Name("servers|global") @Split String[] server, @Name("message") String command) {
        new ServerCommandPacket(Arrays.asList(server), command).send();
        sender.sendMessage(CC.translate("&aExecuted " + command + " on the " + StringUtils.join(server, ", ") + " server(s)"));
    }

}
