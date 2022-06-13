package dev.lbuddyboy.flash.handler;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.command.essentials.*;
import dev.lbuddyboy.flash.command.essentials.chat.ClearChatCommand;
import dev.lbuddyboy.flash.command.essentials.chat.MuteChatCommand;
import dev.lbuddyboy.flash.command.essentials.chat.SlowChatCommand;
import dev.lbuddyboy.flash.command.essentials.message.*;
import dev.lbuddyboy.flash.command.param.GamemodeParam;
import dev.lbuddyboy.flash.command.param.PrefixParam;
import dev.lbuddyboy.flash.command.param.RankParam;
import dev.lbuddyboy.flash.command.param.UUIDParam;
import dev.lbuddyboy.flash.command.rank.RankCommand;
import dev.lbuddyboy.flash.command.server.NotificationsCommand;
import dev.lbuddyboy.flash.command.server.ServersCommand;
import dev.lbuddyboy.flash.command.transport.TransportDataCommand;
import dev.lbuddyboy.flash.command.user.NameMCCommand;
import dev.lbuddyboy.flash.command.user.StaffChatCommand;
import dev.lbuddyboy.flash.command.user.UserCommand;
import dev.lbuddyboy.flash.command.user.grant.GrantCommand;
import dev.lbuddyboy.flash.command.user.grant.GrantsCommand;
import dev.lbuddyboy.flash.command.user.note.NotesCommand;
import dev.lbuddyboy.flash.command.user.prefix.PrefixCommands;
import dev.lbuddyboy.flash.command.user.punishment.*;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBanCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBlacklistCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnIPBanCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnMuteCommand;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.util.YamlDoc;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CommandHandler {

    private final PaperCommandManager commandManager;
    private final Map<String, Command> knownPermissionsMap;
    // Perm // Plugin
    private final YamlDoc commandsYML;

    public CommandHandler() {
        this.commandManager = new PaperCommandManager(Flash.getInstance());
        this.knownPermissionsMap = new ConcurrentHashMap<>();
        this.commandsYML = new YamlDoc(Flash.getInstance().getDataFolder(), "commands.yml");

        this.commandManager.getCommandContexts().registerContext(UUID.class, new UUIDParam());
        this.commandManager.getCommandContexts().registerContext(Rank.class, new RankParam());
        this.commandManager.getCommandContexts().registerContext(GameMode.class, new GamemodeParam());
        this.commandManager.getCommandContexts().registerContext(Prefix.class, new PrefixParam());

        this.commandManager.getCommandCompletions().registerCompletion("target", c -> Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasMetadata("invisible") || !player.hasMetadata("modmode")).map(Player::getName).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("rank", c -> Flash.getInstance().getRankHandler().getRanks().values().stream().map(Rank::getName).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> Arrays.stream(GameMode.values()).map(GameMode::name).map(String::toLowerCase).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("prefixes", c -> Flash.getInstance().getUserHandler().getPrefixes().stream().map(Prefix::getId).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("permissions", c -> this.knownPermissionsMap.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("worlds", c -> Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));

        registerCommand(new MuteChatCommand(), "mutechat");
        registerCommand(new SlowChatCommand(), "slowchat");
        registerCommand(new ClearChatCommand(), "clearchat");

        registerCommand(new BlockCommand(), "block");
        registerCommand(new MessageCommand(), "message");
        registerCommand(new ReplyCommand(), "reply");
        registerCommand(new ToggleMessagesCommand(), "togglemessages");
        registerCommand(new UnBlockCommand(), "unblock");

        registerCommand(new BroadcastCommand(), "broadcast");
        registerCommand(new ClearCommand(), "clear");
        registerCommand(new FeedCommand(), "feed");
        registerCommand(new FlashCommand(), "flash");
        registerCommand(new FreezeCommand(), "freeze");
        registerCommand(new GamemodeCommand(), "gamemode");
        registerCommand(new GMCCommand(), "gmc");
        registerCommand(new GMSCommand(), "gms");
        registerCommand(new HealCommand(), "heal");
        registerCommand(new HelpopCommand(), "helpop");
        registerCommand(new InvseeCommand(), "invsee");
        registerCommand(new MoreCommand(), "more");
        registerCommand(new RawBroadcastCommand(), "rawbroadcast");
        registerCommand(new RenameCommand(), "rename");
        registerCommand(new ReportCommand(), "report");
        registerCommand(new RunCommandCommand(), "runcommand");
        registerCommand(new SudoCommand(), "sudo");
        registerCommand(new TeleportCommand(), "teleport");
        registerCommand(new TeleportHereCommand(), "teleporthere");
        registerCommand(new TeleportPosCommand(), "teleportpos");
        registerCommand(new WorldCommand(), "world");

        registerCommand(new RankCommand(), "rank");

        registerCommand(new PrefixCommands(), "prefix");

        registerCommand(new UnBanCommand(), "unban");
        registerCommand(new UnBlacklistCommand(), "unblacklist");
        registerCommand(new UnIPBanCommand(), "unipban");
        registerCommand(new UnMuteCommand(), "unmute");

        registerCommand(new AltsCommand(), "alts");
        registerCommand(new BanCommand(), "ban");
        registerCommand(new BlacklistCommand(), "blacklist");
        registerCommand(new CheckPunishmentsCommand(), "history");
        registerCommand(new IPBanCommand(), "ipban");
        registerCommand(new KickCommand(), "kick");
        registerCommand(new MuteCommand(), "mute");
        registerCommand(new StaffHistoryCommand(), "staffhistory");
        registerCommand(new WarnCommand(), "warn");

        if (!FlashLanguage.CACHE_TYPE.getString().equalsIgnoreCase("YAML"))
            registerCommand(new NotificationsCommand(), "notifications");
        registerCommand(new ServersCommand(), "servers");

        registerCommand(new TransportDataCommand(), "transport");

        registerCommand(new GrantCommand(), "grant");
        registerCommand(new GrantsCommand(), "grants");
        registerCommand(new StaffChatCommand(), "staffchat");
        registerCommand(new NotesCommand(), "notes");
        registerCommand(new NameMCCommand(), "namemc");
        registerCommand(new UserCommand(), "user");

        Bukkit.getScheduler().runTaskLater(Flash.getInstance(), () -> {
            try {
                Field mapField = SimplePluginManager.class.getDeclaredField("commandMap");
                Field commandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");

                mapField.setAccessible(true);
                commandsField.setAccessible(true);

                SimplePluginManager simplePluginManager = (SimplePluginManager) Bukkit.getServer().getPluginManager();
                SimpleCommandMap simpleCommandMap = (SimpleCommandMap) mapField.get(simplePluginManager);

                Map<String, Command> knownCommands = (Map<String, Command>) commandsField.get(simpleCommandMap);

                for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
                    try {
                        if (entry.getValue() == null) continue;
                        if (entry.getValue().getPermission().equals("bukkit.command.paper;bukkit.command.paper.version;bukkit.command.paper.dumpitem;bukkit.command.paper.heap;bukkit.command.paper.chunkinfo;bukkit.command.paper.syncloadinfo;bukkit.command.paper.fixlight;bukkit.command.paper.dumpwait"))
                        knownPermissionsMap.put(entry.getValue().getPermission(), entry.getValue());
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }, 10);
    }
    
    private void registerCommand(BaseCommand command, String section) {
        if (!this.commandsYML.gc().contains("commands." + section)) {
            this.commandsYML.gc().set("commands." + section + ".enabled", true);
            this.commandsYML.gc().set("commands." + section + ".permission", "flash.command." + section);
            try {
                this.commandsYML.save();
            } catch (IOException ignored) {

            }
        }
        if (this.commandsYML.gc().getBoolean("commands." + section + ".enabled")) {
            this.commandManager.registerCommand(command);
        }
    }

}
