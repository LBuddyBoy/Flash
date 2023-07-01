package dev.lbuddyboy.flash.command.param

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import dev.lbuddyboy.flash.Flash
import java.util.*

class UUIDParam : ContextResolver<UUID, BukkitCommandExecutionContext> {
    @Throws(InvalidCommandArgument::class)
    override fun getContext(arg: BukkitCommandExecutionContext): UUID {
        val sender = arg.player
        val source = arg.popFirstArg()
        if (sender != null && (source.equals("self", ignoreCase = true) || source == "")) {
            return sender.uniqueId
        }
        val uuid: UUID = Flash.instance.cacheHandler.getUserCache().getUUID(source)
        if (uuid != null) return uuid
        throw InvalidCommandArgument("No player with the name $source could be found.")
    }
}