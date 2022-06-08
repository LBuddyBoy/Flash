package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.packet.ServerBroadcastPacket;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@CommandAlias("rawbroadcast|rawbc|rawalert")
public class RawBroadcastCommand extends BaseCommand {

    @Default
    public static void broadcast(CommandSender sender, @Name("servers|global") @Split String[] server, @Name("message") String message) {
        new ServerBroadcastPacket(Arrays.asList(server), CC.translate(message)).send();
    }

}
