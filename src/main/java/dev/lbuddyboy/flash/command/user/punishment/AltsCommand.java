package dev.lbuddyboy.flash.command.user.punishment;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandAlias("alts|dupeip")
@CommandPermission("flash.command.alts")
public class AltsCommand extends BaseCommand {

    @Default
    @CommandCompletion("@target")
    public static void alts(CommandSender sender, @Name("target") UUID uuid) {
        User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

        if (user == null) {
            sender.sendMessage(CC.translate(FlashLanguage.INVALID_USER.getString()));
            return;
        }

        List<UUID> alts = new ArrayList<>();
        // This might lag, but I still need to do some stress tests with it.
        Flash.getInstance().getUserHandler().relativeAlts(user.getIp(), alts);

        if (alts.size() <= 0) {
            sender.sendMessage(CC.translate(user.getColoredName() + "&c does not have any alts."));
            return;
        }

        List<String> coloredAlts = alts.stream().map(alt -> Flash.getInstance().getUserHandler().tryUser(alt, true)).map(User::colorAlt).collect(Collectors.toList());

        sender.sendMessage(CC.translate("" + user.getColoredName() + "'s &4Alts:"));
        sender.sendMessage(CC.translate("&7Offline &7- &aOnline &7- &6Muted &7- &cBanned &7- &eIP-Banned &7- &4Blacklisted"));
        sender.sendMessage(CC.translate("&7> " + StringUtils.join(coloredAlts, "&7, ")));

    }

}
