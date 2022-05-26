package dev.lbuddyboy.flash.command.param;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RankParam implements ContextResolver<Rank, BukkitCommandExecutionContext> {

    @Override
    public Rank getContext(BukkitCommandExecutionContext arg) throws InvalidCommandArgument {
        String source = arg.popFirstArg();

        try {
            return Flash.getInstance().getRankHandler().getRank(source);
        } catch (Exception ignored) {
            throw new InvalidCommandArgument(CC.translate(FlashLanguage.RANK_EXISTS.getString()));
        }
    }
}