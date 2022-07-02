package dev.lbuddyboy.flash.command.user;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.command.essentials.FreezeCommand;
import dev.lbuddyboy.flash.command.user.punishment.BlacklistCommand;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@CommandAlias("sync")
public class SyncCommand extends BaseCommand {

    @Default
    public static void sync(Player sender) {
        if (!FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("MONGO")) {
            sender.sendMessage(CC.translate("&cYou need mongo for this feature to work."));
            return;
        }

        User user = Flash.getInstance().getUserHandler().tryUser(sender.getUniqueId(), false);

        if (user.isDiscordSynced()) {
//            if (user.getDiscordRank() == null && user.getActiveRank().isStaff() || sender.isOp() || sender.hasPermission("*") || sender.hasPermission("flash.staff")) {
//                sender.sendMessage(CC.translate("&cFailed to verify who you are. You are now banned."));
//                BlacklistCommand.blacklist(Bukkit.getConsoleSender(), sender.getUniqueId(), "perm", "Failed sync verify.");
//                return;
//            }

            String discord = user.getDiscordSyncedName();
            sender.sendMessage(CC.translate("&aYour minecraft account is currently synced to " + discord + "."));

//            if (sender.hasMetadata("frozen")) {
//                FreezeCommand.freeze(Bukkit.getConsoleSender(), sender);
//            }

            return;
        }

        int code = random();
        user.getPlayerInfo().setSyncCode(code);

        Document document = new Document();

        document.put("player", sender.getUniqueId().toString());
        document.put("playerName", sender.getName());
        document.put("code", code);
        document.put("createdAt", System.currentTimeMillis());

        Flash.getInstance().getMongoHandler().getSyncCodesCollection().replaceOne(Filters.eq("player", sender.getUniqueId().toString()), document, new ReplaceOptions().upsert(true));

        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &aYour Discord sync code is " + user.getPlayerInfo().getSyncCode() + "."));
        sender.sendMessage(CC.translate(" &aJoin discord.steelpvp.com & type this code in #sync."));
        sender.sendMessage("");

    }

    @Subcommand("delete")
    @CommandPermission("sync.delete")
    @CommandCompletion("@target")
    public static void resetSync(CommandSender sender, @Name("target") UUID target) {
        Flash.getInstance().getMongoHandler().getSyncCollection().deleteOne(Filters.eq("playerUUID", target.toString()));
    }

    public static int random() {
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);

        if (Flash.getInstance().getMongoHandler().getSyncCodesCollection().find(Filters.eq("code", random)).first() != null) {
            return random();
        }

        return random;
    }

}
