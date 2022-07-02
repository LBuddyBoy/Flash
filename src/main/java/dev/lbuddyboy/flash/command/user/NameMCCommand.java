package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.NameMCUtils;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("namemc|claimrank|claimfreerank|freerank")
public class NameMCCommand extends BaseCommand {

    @Default
    public static void namemc(Player sender) {

        User user = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        if (user.getPlayerInfo().isClaimedNameMC()) {
            sender.sendMessage(CC.translate("&cYou've already claimed a free rank."));
            return;
        }

        if (!NameMCUtils.hasLiked(sender)) {
            sender.sendMessage(CC.translate("&cYou need to like " + FlashLanguage.SERVER_IP.getString() + " on NameMC"));
            sender.sendMessage(CC.translate("&c - https://namemc.com/server/" + FlashLanguage.SERVER_IP.getString() + "/"));
            return;
        }

        for (String s : CC.applyTarget(FlashLanguage.NAMEMC_RANK_CLAIM_COMMANDS.getStringList(), user.getUuid())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
        }
        user.getPlayerInfo().setClaimedNameMC(true);
        user.save(true);
    }

}
