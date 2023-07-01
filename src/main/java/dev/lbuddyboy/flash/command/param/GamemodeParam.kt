package dev.lbuddyboy.flash.command.param

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.GameMode
import java.util.*

class GamemodeParam : ContextResolver<GameMode, BukkitCommandExecutionContext> {
    @Throws(InvalidCommandArgument::class)
    override fun getContext(arg: BukkitCommandExecutionContext): GameMode {
        val sender = arg.player
        val source = arg.popFirstArg()
        return try {
            var gameMode = GameMode.valueOf(source.uppercase(Locale.getDefault()))
            if (GameMode.getByValue(gameMode.value) == null) {
                gameMode = GameMode.getByValue(source.toInt())
            }
            gameMode
        } catch (ignored: Exception) {
            throw InvalidCommandArgument("No gamemode with the name $source found.")
        }
    }
}