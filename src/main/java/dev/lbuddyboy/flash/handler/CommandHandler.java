package dev.lbuddyboy.flash.handler;

import co.aikar.commands.PaperCommandManager;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.command.essentials.*;
import dev.lbuddyboy.flash.command.essentials.chat.MuteChatCommand;
import dev.lbuddyboy.flash.command.essentials.chat.SlowChatCommand;
import dev.lbuddyboy.flash.command.essentials.message.*;
import dev.lbuddyboy.flash.command.param.GamemodeParam;
import dev.lbuddyboy.flash.command.param.PrefixParam;
import dev.lbuddyboy.flash.command.param.RankParam;
import dev.lbuddyboy.flash.command.param.UUIDParam;
import dev.lbuddyboy.flash.command.rank.RankCommand;
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
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CommandHandler {

    private final PaperCommandManager commandManager;
    private final Map<String, Command> knownPermissionsMap;
                     // Perm // Plugin

    public CommandHandler() {
        this.commandManager = new PaperCommandManager(Flash.getInstance());
        this.knownPermissionsMap = new ConcurrentHashMap<>();

        this.commandManager.getCommandContexts().registerContext(UUID.class, new UUIDParam());
        this.commandManager.getCommandContexts().registerContext(Rank.class, new RankParam());
        this.commandManager.getCommandContexts().registerContext(GameMode.class, new GamemodeParam());
        this.commandManager.getCommandContexts().registerContext(Prefix.class, new PrefixParam());

        this.commandManager.getCommandCompletions().registerCompletion("target", c -> Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasMetadata("invisible") || !player.hasMetadata("modmode")).map(Player::getName).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("rank", c -> Flash.getInstance().getRankHandler().getRanks().values().stream().map(Rank::getName).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> Arrays.stream(GameMode.values()).map(GameMode::name).map(String::toLowerCase).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("prefixes", c -> Flash.getInstance().getUserHandler().getPrefixes().stream().map(Prefix::getId).collect(Collectors.toList()));

        this.commandManager.registerCommand(new MuteChatCommand());
        this.commandManager.registerCommand(new SlowChatCommand());

        this.commandManager.registerCommand(new BlockCommand());
        this.commandManager.registerCommand(new MessageCommand());
        this.commandManager.registerCommand(new ReplyCommand());
        this.commandManager.registerCommand(new ToggleMessagesCommand());
        this.commandManager.registerCommand(new UnBlockCommand());

        this.commandManager.registerCommand(new BroadcastCommand());
        this.commandManager.registerCommand(new ClearCommand());
        this.commandManager.registerCommand(new FeedCommand());
        this.commandManager.registerCommand(new FreezeCommand());
        this.commandManager.registerCommand(new GamemodeCommand());
        this.commandManager.registerCommand(new GMCCommand());
        this.commandManager.registerCommand(new GMSCommand());
        this.commandManager.registerCommand(new HealCommand());
        this.commandManager.registerCommand(new HelpopCommand());
        this.commandManager.registerCommand(new InvseeCommand());
        this.commandManager.registerCommand(new MoreCommand());
        this.commandManager.registerCommand(new RawBroadcastCommand());
        this.commandManager.registerCommand(new RenameCommand());
        this.commandManager.registerCommand(new ReportCommand());
        this.commandManager.registerCommand(new RunCommandCommand());
        this.commandManager.registerCommand(new SudoCommand());
        this.commandManager.registerCommand(new TeleportCommand());
        this.commandManager.registerCommand(new TeleportHereCommand());
        this.commandManager.registerCommand(new TeleportPosCommand());

        this.commandManager.registerCommand(new RankCommand());

        this.commandManager.registerCommand(new PrefixCommands());

        this.commandManager.registerCommand(new UnBanCommand());
        this.commandManager.registerCommand(new UnBlacklistCommand());
        this.commandManager.registerCommand(new UnIPBanCommand());
        this.commandManager.registerCommand(new UnMuteCommand());

        this.commandManager.registerCommand(new AltsCommand());
        this.commandManager.registerCommand(new BanCommand());
        this.commandManager.registerCommand(new BlacklistCommand());
        this.commandManager.registerCommand(new CheckPunishmentsCommand());
        this.commandManager.registerCommand(new IPBanCommand());
        this.commandManager.registerCommand(new KickCommand());
        this.commandManager.registerCommand(new MuteCommand());
        this.commandManager.registerCommand(new StaffHistoryCommand());
        this.commandManager.registerCommand(new WarnCommand());

        this.commandManager.registerCommand(new ServersCommand());

        this.commandManager.registerCommand(new TransportDataCommand());

        this.commandManager.registerCommand(new GrantCommand());
        this.commandManager.registerCommand(new GrantsCommand());
        this.commandManager.registerCommand(new StaffChatCommand());
        this.commandManager.registerCommand(new NotesCommand());
        this.commandManager.registerCommand(new NameMCCommand());
        this.commandManager.registerCommand(new UserCommand());

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
                        knownPermissionsMap.put(entry.getValue().getPermission(), entry.getValue());
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
        }, 10);
    }

}
