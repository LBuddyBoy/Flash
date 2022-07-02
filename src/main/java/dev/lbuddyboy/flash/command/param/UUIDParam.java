package dev.lbuddyboy.flash.command.param;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import dev.lbuddyboy.flash.Flash;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UUIDParam implements ContextResolver<UUID, BukkitCommandExecutionContext> {

    @Override
    public UUID getContext(BukkitCommandExecutionContext arg) throws InvalidCommandArgument {
        Player sender = arg.getPlayer();
        String source = arg.popFirstArg();

        if (sender != null && (source.equalsIgnoreCase("self") || source.equals(""))) {
            return sender.getUniqueId();
        }

        UUID uuid = Flash.getInstance().getCacheHandler().getUserCache().getUUID(source);
        if (uuid != null) return uuid;

        throw new InvalidCommandArgument("No player with the name " + source + " could be found.");
    }
}