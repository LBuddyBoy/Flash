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

@CommandAlias("report")
@CommandPermission("flash.command.report")
public class ReportCommand extends BaseCommand {

    @Default
    public static void report(Player sender, @Name("target") Player target, @Name("message") String reason) {
        User user = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        if (user.getPlayerInfo().getLastRequestSent() > System.currentTimeMillis()) {
            sender.sendMessage(CC.translate("&cYou cannot do this for another " + TimeUtils.formatLongIntoDetailedString((user.getPlayerInfo().getLastRequestSent() - System.currentTimeMillis()) / 1000)));
            return;
        }

        new StaffMessageListPacket(CC.applyPlayer(CC.applyTarget(FlashLanguage.ESSENTIALS_REPORT_STAFF_MESSAGE.getStringList(), target.getUniqueId()), sender.getUniqueId()), new Object[]{"%SERVER%", FlashLanguage.SERVER_NAME.getString(), "%REASON%", reason}).send();

        sender.sendMessage(CC.translate(FlashLanguage.ESSENTIALS_HELPOP_SENDER_MESSAGE.getString()));
        user.getPlayerInfo().setLastRequestSent(System.currentTimeMillis() + 60_000L);

    }

}
