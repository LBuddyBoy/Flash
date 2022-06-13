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

@CommandAlias("mutechat|chatmute|silencechat|togglechat")
@CommandPermission("flash.command.clearchat")
public class MuteChatCommand extends BaseCommand {

    @Default
    public static void def(CommandSender sender) {
        FlashLanguage.CHAT_MUTED.update(!FlashLanguage.CHAT_MUTED.getBoolean());
        Flash.getInstance().getChatHandler().setChatMuted(FlashLanguage.CHAT_MUTED.getBoolean());

        sender.sendMessage(CC.translate("&aChat is now " + (FlashLanguage.CHAT_MUTED.getBoolean() ? "muted" : "un-muted")));
    }

}
