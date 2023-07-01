package dev.lbuddyboy.flash.command.param

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.user.model.*
import dev.lbuddyboy.flash.util.bukkit.CC

class PrefixParam : ContextResolver<Prefix, BukkitCommandExecutionContext> {
    @Throws(InvalidCommandArgument::class)
    override fun getContext(arg: BukkitCommandExecutionContext): Prefix {
        val source = arg.popFirstArg()
        val prefix = Flash.instance.userHandler.getPrefix(source)
        if (prefix != null) return prefix
        throw InvalidCommandArgument(CC.translate("&cNo prefix with that name exists."))
    }
}