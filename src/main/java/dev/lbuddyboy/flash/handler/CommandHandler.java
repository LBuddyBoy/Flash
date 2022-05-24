package dev.lbuddyboy.flash.handler;

import co.aikar.commands.PaperCommandManager;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.command.param.UUIDParam;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.command.RankCommand;
import dev.lbuddyboy.flash.rank.command.param.RankParam;
import dev.lbuddyboy.flash.user.command.UserCommand;
import dev.lbuddyboy.flash.user.grant.command.GrantCommand;
import dev.lbuddyboy.flash.user.grant.command.GrantsCommand;
import dev.lbuddyboy.flash.user.punishment.command.*;
import dev.lbuddyboy.flash.user.punishment.command.resolve.UnBanCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CommandHandler {

    private final PaperCommandManager commandManager;

    public CommandHandler() {
        this.commandManager = new PaperCommandManager(Flash.getInstance());

        this.commandManager.getCommandContexts().registerContext(UUID.class, new UUIDParam());
        this.commandManager.getCommandContexts().registerContext(Rank.class, new RankParam());

        this.commandManager.getCommandCompletions().registerCompletion("target", c -> {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasMetadata("invisible") || player.hasMetadata("modmode")) continue;
                players.add(player.getName());
            }
            return players;
        });

        this.commandManager.getCommandCompletions().registerCompletion("rank", c -> {
            List<String> ranks = new ArrayList<>();
            for (Rank rank : Flash.getInstance().getRankHandler().getRanks().values()) {
                ranks.add(rank.getName());
            }

            return ranks;
        });

        this.commandManager.registerCommand(new UnBanCommand());

        this.commandManager.registerCommand(new BanCommand());
        this.commandManager.registerCommand(new BlacklistCommand());
        this.commandManager.registerCommand(new CheckPunishmentsCommand());
        this.commandManager.registerCommand(new IPBanCommand());
        this.commandManager.registerCommand(new KickCommand());
        this.commandManager.registerCommand(new MuteCommand());
        this.commandManager.registerCommand(new WarnCommand());

        this.commandManager.registerCommand(new RankCommand());
        this.commandManager.registerCommand(new GrantCommand());
        this.commandManager.registerCommand(new GrantsCommand());
        this.commandManager.registerCommand(new UserCommand());
    }

}
