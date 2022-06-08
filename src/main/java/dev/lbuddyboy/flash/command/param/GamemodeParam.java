package dev.lbuddyboy.flash.command.param;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import dev.lbuddyboy.flash.Flash;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamemodeParam implements ContextResolver<GameMode, BukkitCommandExecutionContext> {

    @Override
    public GameMode getContext(BukkitCommandExecutionContext arg) throws InvalidCommandArgument {
        Player sender = arg.getPlayer();
        String source = arg.popFirstArg();

        try {
            return GameMode.valueOf(source.toUpperCase());
        } catch (Exception ignored) {
            throw new InvalidCommandArgument("No gamemode with the name " + source + " found.");
        }
    }
}