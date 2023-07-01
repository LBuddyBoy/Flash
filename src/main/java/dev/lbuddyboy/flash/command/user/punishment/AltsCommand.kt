package dev.lbuddyboy.flash.command.user.punishment;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.PunishmentType;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (sender instanceof Player) {
            User senderUser = Flash.getInstance().getUserHandler().tryUser(((Player) sender).getUniqueId(), true);

            if (senderUser.getActiveRank().getWeight() < user.getActiveRank().getWeight()) {
                sender.sendMessage(CC.translate(FlashLanguage.PUNISHMENTS_HIGHER_PRIORITY.getString()));
                return;
            }
        }

        List<UUID> alts = Flash.getInstance().getUserHandler().relativeAlts(user.getIp());

        List<String> coloredAlts = alts.stream().map(alt -> Flash.getInstance().getUserHandler().tryUser(alt, true)).map(User::colorAlt).collect(Collectors.toList());
        String translate = StringUtils.join(Arrays.stream(PunishmentType.values()).map(type -> type.getColor() + type.getPlural()).collect(Collectors.toList()), "&7 - ");

        sender.sendMessage(CC.translate("" + user.getColoredName() + "'s &4Alts:"));
        sender.sendMessage(CC.translate("&7Offline &7- " + translate));
        sender.sendMessage(CC.translate("&7> " + (alts.size() <= 0 ? "&cNone" : StringUtils.join(coloredAlts, "&7, "))));

    }

}
