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

        try {
            return Flash.getInstance().getCacheHandler().getUserCache().getUUID(source);
        } catch (Exception ignored) {

        }

        throw new InvalidCommandArgument("No person with the name " + source + " found.");
    }
}