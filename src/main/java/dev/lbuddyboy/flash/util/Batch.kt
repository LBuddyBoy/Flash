package dev.lbuddyboy.flash.util

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.bukkit.command.CommandSender

@AllArgsConstructor
@Getter
class Batch {
    private val name: String? = null
    private val sender: CommandSender? = null
    private val callbacks: List<Callable>? = null
    private val startedAt: Long = 0

    @Setter
    private val done = false
}