package dev.lbuddyboy.flash.command.param

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.FlashLanguage
import dev.lbuddyboy.flash.rank.Rank
import dev.lbuddyboy.flash.util.bukkit.CC

class RankParam : ContextResolver<Rank, BukkitCommandExecutionContext> {
    @Throws(InvalidCommandArgument::class)
    override fun getContext(arg: BukkitCommandExecutionContext): Rank {
        val source = arg.popFirstArg()
        val rank = Flash.instance.rankHandler.getRank(source)
        if (rank != null) return rank
        throw InvalidCommandArgument(CC.translate(FlashLanguage.RANK_EXISTS.string))
    }
}