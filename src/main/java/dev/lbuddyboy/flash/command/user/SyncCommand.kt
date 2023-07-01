package dev.lbuddyboy.flash.command.user

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.user.User
import dev.lbuddyboy.flash.util.bukkit.CC
import org.bson.Document
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@CommandAlias("sync")
object SyncCommand : BaseCommand() {
    @Default
    fun sync(sender: Player) {
        if (!FlashLanguage.CACHE_TYPE.string.equals("MONGO", ignoreCase = true)) {
            sender.sendMessage(CC.translate("&cYou need mongo for this feature to work."))
            return
        }
        val user: User = Flash.instance.userHandler.tryUser(sender.uniqueId, false)
        if (user.isDiscordSynced) {
//            if (user.getDiscordRank() == null && user.getActiveRank().isStaff() || sender.isOp() || sender.hasPermission("*") || sender.hasPermission("flash.staff")) {
//                sender.sendMessage(CC.translate("&cFailed to verify who you are. You are now banned."));
//                BlacklistCommand.blacklist(Bukkit.getConsoleSender(), sender.getUniqueId(), "perm", "Failed sync verify.");
//                return;
//            }
            val discord = user.discordSyncedName
            sender.sendMessage(CC.translate("&aYour minecraft account is currently synced to $discord."))

//            if (sender.hasMetadata("frozen")) {
//                FreezeCommand.freeze(Bukkit.getConsoleSender(), sender);
//            }
            return
        }
        val code = random()
        user.getPlayerInfo().syncCode = code.toLong()
        val document = Document()
        document["player"] = sender.uniqueId.toString()
        document["playerName"] = sender.name
        document["code"] = code
        document["createdAt"] = System.currentTimeMillis()
        Flash.instance.mongoHandler.getSyncCodesCollection()
            .replaceOne(Filters.eq("player", sender.uniqueId.toString()), document, ReplaceOptions().upsert(true))
        sender.sendMessage("")
        sender.sendMessage(CC.translate(" &aYour Discord sync code is " + user.getPlayerInfo().syncCode + "."))
        sender.sendMessage(CC.translate(" &aJoin discord.steelpvp.com & type this code in #sync."))
        sender.sendMessage("")
    }

    @Subcommand("delete")
    @CommandPermission("sync.delete")
    @CommandCompletion("@target")
    fun resetSync(sender: CommandSender?, @Name("target") target: UUID) {
        Flash.instance.mongoHandler.getSyncCollection().deleteOne(Filters.eq("playerUUID", target.toString()))
    }

    fun random(): Int {
        val random = ThreadLocalRandom.current().nextInt(1000, 10000)
        return if (Flash.instance.mongoHandler.getSyncCodesCollection().find(Filters.eq("code", random))
                .first() != null
        ) {
            random()
        } else random
    }
}