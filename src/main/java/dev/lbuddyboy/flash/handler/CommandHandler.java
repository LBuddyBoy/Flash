package dev.lbuddyboy.flash.handler;

import co.aikar.commands.PaperCommandManager;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.command.param.UUIDParam;
import dev.lbuddyboy.flash.command.user.punishment.*;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBlacklistCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnIPBanCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnMuteCommand;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.command.rank.RankCommand;
import dev.lbuddyboy.flash.command.param.RankParam;
import dev.lbuddyboy.flash.command.user.UserCommand;
import dev.lbuddyboy.flash.command.user.grant.GrantCommand;
import dev.lbuddyboy.flash.command.user.grant.GrantsCommand;
import dev.lbuddyboy.flash.command.user.punishment.resolve.UnBanCommand;
import dev.lbuddyboy.flash.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CommandHandler {

    private final PaperCommandManager commandManager;
    private final Map<String, String> knowPermissionsMap;
                     // Perm // Plugin

    public CommandHandler() {
        this.commandManager = new PaperCommandManager(Flash.getInstance());
        this.knowPermissionsMap = new ConcurrentHashMap<>();

        this.commandManager.getCommandContexts().registerContext(UUID.class, new UUIDParam());
        this.commandManager.getCommandContexts().registerContext(Rank.class, new RankParam());

        this.commandManager.getCommandCompletions().registerCompletion("target", c -> Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasMetadata("invisible") || !player.hasMetadata("modmode")).map(Player::getName).collect(Collectors.toList()));
        this.commandManager.getCommandCompletions().registerCompletion("rank", c -> Flash.getInstance().getRankHandler().getRanks().values().stream().map(Rank::getName).collect(Collectors.toList()));

        this.commandManager.registerCommand(new RankCommand());

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
        this.commandManager.registerCommand(new WarnCommand());

        this.commandManager.registerCommand(new GrantCommand());
        this.commandManager.registerCommand(new GrantsCommand());
        this.commandManager.registerCommand(new UserCommand());

        Bukkit.getScheduler().runTaskLater(Flash.getInstance(), () -> {
            for (Plugin plugin : Flash.getInstance().getServer().getPluginManager().getPlugins()) {
                try {
                    /*
                    TODO: Work on this :D
                     */
//                    Field commandMapField = Class.forName("org.bukkit.craftbukkit." );
                } catch (Exception ignored) {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cError loading all known permissions module."));
                }
            }
        }, 10);
    }

}
