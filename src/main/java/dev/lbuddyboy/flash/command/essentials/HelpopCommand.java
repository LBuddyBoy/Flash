package dev.lbuddyboy.flash.command.essentials;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.packet.StaffMessageListPacket;
import dev.lbuddyboy.flash.user.packet.StaffMessagePacket;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.entity.Player;

@CommandAlias("helpop|request")
@CommandPermission("flash.command.helpop")
public class HelpopCommand extends BaseCommand {

    @Default
    public static void helpop(Player sender, @Name("message") String reason) {
        User user = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        if (user.getPlayerInfo().getLastRequestSent() > System.currentTimeMillis()) {
            sender.sendMessage(CC.translate("&cYou cannot do this for another " + TimeUtils.formatLongIntoDetailedString(user.getPlayerInfo().getLastRequestSent() - System.currentTimeMillis() / 1000)));
            return;
        }

        new StaffMessageListPacket(CC.applyPlayer(FlashLanguage.ESSENTIALS_HELPOP_STAFF_MESSAGE.getStringList(), sender.getUniqueId(),
                "%SERVER%", FlashLanguage.SERVER_NAME.getString(), "%REASON%", reason)).send();

        sender.sendMessage(CC.translate(FlashLanguage.ESSENTIALS_HELPOP_SENDER_MESSAGE.getString()));
        user.getPlayerInfo().setLastRequestSent(System.currentTimeMillis() + 60_000L);

    }

}
