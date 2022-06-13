package dev.lbuddyboy.flash.command.essentials.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.command.CommandSender;

@CommandAlias("slowchat|chatslow|delaychat|chatdelay")
@CommandPermission("flash.command.clearchat")
public class SlowChatCommand extends BaseCommand {

    @Default
    public static void def(CommandSender sender, @Name("seconds") int seconds) {
        sender.sendMessage(CC.translate("&aChat's message delay is now " + seconds + " seconds."));
        FlashLanguage.CHAT_SLOW.update(seconds);
        Flash.getInstance().getChatHandler().setSlowChat(seconds);
    }

}
