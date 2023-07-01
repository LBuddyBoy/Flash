package dev.lbuddyboy.flash.handler

import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.PaperCommandManager
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.command.essentials.*
import dev.lbuddyboy.flash.command.essentials.chat.ClearChatCommand
import dev.lbuddyboy.flash.command.essentials.chat.MuteChatCommand
import dev.lbuddyboy.flash.command.essentials.chat.SlowChatCommand
import dev.lbuddyboy.flash.command.essentials.message.*
import dev.lbuddyboy.flash.command.param.GamemodeParam
import dev.lbuddyboy.flash.command.param.PrefixParam
import dev.lbuddyboy.flash.command.param.RankParam
import dev.lbuddyboy.flash.command.param.UUIDParam
import dev.lbuddyboy.flash.command.rank.RankCommand
import dev.lbuddyboy.flash.command.server.NotificationsCommand
import dev.lbuddyboy.flash.command.server.ServersCommand
import dev.lbuddyboy.flash.command.transport.TransportDataCommand
import dev.lbuddyboy.flash.command.user.*
import dev.lbuddyboy.flash.command.user.grant.GrantCommand
import dev.lbuddyboy.flash.command.user.grant.GrantsCommand
import dev.lbuddyboy.flash.command.user.note.NotesCommand
import dev.lbuddyboy.flash.command.user.prefix.PrefixCommands
import dev.lbuddyboy.flash.command.user.punishment.*
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBanCommand
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBlacklistCommand
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnIPBanCommand
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnMuteCommand
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.user.model.Prefix
import dev.lbuddyboy.flash.util.YamlDoc
import lombok.Getter
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.SimplePluginManager
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Getter
class CommandHandler {
    private val commandManager: PaperCommandManager
    private val knownPermissionsMap: MutableMap<String, Command?>

    // Perm // Plugin
    private val commandsYML: YamlDoc

    init {
        commandManager = PaperCommandManager(Flash.instance)
        knownPermissionsMap = ConcurrentHashMap()
        commandsYML = YamlDoc(Flash.instance.dataFolder, "commands.yml")
        commandManager.commandContexts.registerContext(UUID::class.java, UUIDParam())
        commandManager.commandContexts.registerContext(Rank::class.java, RankParam())
        commandManager.commandContexts.registerContext(GameMode::class.java, GamemodeParam())
        commandManager.commandContexts.registerContext(Prefix::class.java, PrefixParam())
        commandManager.commandCompletions.registerCompletion("target") { c: BukkitCommandCompletionContext? ->
            Bukkit.getOnlinePlayers().stream()
                .filter { player: Player -> !player.hasMetadata("invisible") || !player.hasMetadata("modmode") }
                .map { obj: Player -> obj.name }.collect(Collectors.toList())
        }
        commandManager.commandCompletions.registerCompletion("rank") { c: BukkitCommandCompletionContext? ->
            Flash.instance.rankHandler.ranks.values.stream().map { obj: Rank -> obj.getName() }
                .collect(Collectors.toList())
        }
        commandManager.commandCompletions.registerCompletion("gamemodes") { c: BukkitCommandCompletionContext? ->
            Arrays.stream(GameMode.values()).map { obj: GameMode -> obj.name }
                .map { obj: String -> obj.lowercase(Locale.getDefault()) }.collect(Collectors.toList())
        }
        commandManager.commandCompletions.registerCompletion("prefixes") { c: BukkitCommandCompletionContext? ->
            Flash.instance.userHandler.prefixes.stream().map { obj: Prefix -> obj.id }
                .collect(Collectors.toList())
        }
        commandManager.commandCompletions.registerCompletion("permissions") { c: BukkitCommandCompletionContext? ->
            knownPermissionsMap.keys.stream().sorted(
                Comparator.naturalOrder()
            ).collect(Collectors.toList())
        }
        commandManager.commandCompletions.registerCompletion("worlds") { c: BukkitCommandCompletionContext? ->
            Bukkit.getWorlds().stream().map { obj: World -> obj.name }
                .collect(Collectors.toList())
        }
        registerCommand(MuteChatCommand(), "mutechat")
        registerCommand(SlowChatCommand(), "slowchat")
        registerCommand(ClearChatCommand(), "clearchat")
        registerCommand(BlockCommand(), "block")
        registerCommand(MessageCommand(), "message")
        registerCommand(ReplyCommand(), "reply")
        registerCommand(ToggleMessagesCommand(), "togglemessages")
        registerCommand(UnBlockCommand(), "unblock")
        registerCommand(BroadcastCommand(), "broadcast")
        registerCommand(ClearCommand(), "clear")
        registerCommand(FeedCommand(), "feed")
        registerCommand(FlashCommand(), "flash")
        registerCommand(FreezeCommand(), "freeze")
        registerCommand(GamemodeCommand(), "gamemode")
        registerCommand(GMCCommand(), "gmc")
        registerCommand(GMSCommand(), "gms")
        registerCommand(HealCommand(), "heal")
        registerCommand(HelpopCommand(), "helpop")
        registerCommand(InvseeCommand(), "invsee")
        registerCommand(MoreCommand(), "more")
        registerCommand(RawBroadcastCommand(), "rawbroadcast")
        registerCommand(RenameCommand(), "rename")
        registerCommand(ReportCommand(), "report")
        registerCommand(SudoCommand(), "sudo")
        registerCommand(TeleportCommand(), "teleport")
        registerCommand(TeleportHereCommand(), "teleporthere")
        registerCommand(TeleportPosCommand(), "teleportpos")
        registerCommand(WorldCommand(), "world")
        registerCommand(LookUpCommand(), "lookup")
        registerCommand(RankCommand(), "rank")
        registerCommand(PrefixCommands(), "prefix")
        registerCommand(UnBanCommand(), "unban")
        registerCommand(UnBlacklistCommand(), "unblacklist")
        registerCommand(UnIPBanCommand(), "unipban")
        registerCommand(UnMuteCommand(), "unmute")
        registerCommand(AltsCommand(), "alts")
        registerCommand(BanCommand(), "ban")
        registerCommand(BlacklistCommand(), "blacklist")
        registerCommand(CheckPunishmentsCommand(), "history")
        registerCommand(IPBanCommand(), "ipban")
        registerCommand(KickCommand(), "kick")
        registerCommand(MuteCommand(), "mute")
        registerCommand(StaffHistoryCommand(), "staffhistory")
        registerCommand(WarnCommand(), "warn")
        if (!FlashLanguage.CACHE_TYPE.string.equals("YAML", ignoreCase = true)) registerCommand(
            NotificationsCommand(),
            "notifications"
        )
        registerCommand(ServersCommand(), "servers")
        registerCommand(TransportDataCommand(), "transport")
        registerCommand(GrantCommand(), "grant")
        registerCommand(GrantsCommand(), "grants")
        registerCommand(StaffChatCommand(), "staffchat")
        registerCommand(NotesCommand(), "notes")
        registerCommand(NameMCCommand(), "namemc")
        registerCommand(SyncCommand(), "sync")
        registerCommand(UserCommand(), "user")
        Bukkit.getScheduler().runTaskLater(Flash.instance, {
            try {
                val mapField = SimplePluginManager::class.java.getDeclaredField("commandMap")
                val commandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
                mapField.isAccessible = true
                commandsField.isAccessible = true
                val simplePluginManager = Bukkit.getServer().pluginManager as SimplePluginManager
                val simpleCommandMap = mapField[simplePluginManager] as SimpleCommandMap
                val knownCommands = commandsField[simpleCommandMap] as Map<String, Command?>
                for ((_, value) in knownCommands) {
                    try {
                        if (value == null) continue
                        if (value.permission == "bukkit.command.paper;bukkit.command.paper.version;bukkit.command.paper.dumpitem;bukkit.command.paper.heap;bukkit.command.paper.chunkinfo;bukkit.command.paper.syncloadinfo;bukkit.command.paper.fixlight;bukkit.command.paper.dumpwait") continue
                        knownPermissionsMap[value.permission] = value
                    } catch (ignored: Exception) {
                    }
                }
            } catch (ignored: Exception) {
            }
        }, 10)
    }

    private fun registerCommand(command: BaseCommand, section: String) {
        if (!commandsYML.gc().contains("commands.$section")) {
            commandsYML.gc()["commands.$section.enabled"] = true
            commandsYML.gc()["commands.$section.permission"] = "flash.command.$section"
            try {
                commandsYML.save()
            } catch (ignored: IOException) {
            }
        }
        if (commandsYML.gc().getBoolean("commands.$section.enabled")) {
            commandManager.registerCommand(command)
        }
    }
}