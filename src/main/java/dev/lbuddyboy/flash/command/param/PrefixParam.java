package dev.lbuddyboy.flash.command.param;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.model.Prefix;
import dev.lbuddyboy.flash.util.bukkit.CC;

public class PrefixParam implements ContextResolver<Prefix, BukkitCommandExecutionContext> {

    @Override
    public Prefix getContext(BukkitCommandExecutionContext arg) throws InvalidCommandArgument {
        String source = arg.popFirstArg();

        try {
            return Flash.getInstance().getUserHandler().getPrefix(source);
        } catch (Exception ignored) {
            throw new InvalidCommandArgument(CC.translate("&cNo prefix with that name exists."));
        }
    }
}