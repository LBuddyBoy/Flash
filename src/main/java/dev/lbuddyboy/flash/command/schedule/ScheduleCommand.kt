package dev.lbuddyboy.flash.command.schedule

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.schedule.Task
import org.bukkit.command.CommandSender
import java.util.*

@CommandAlias("schedule|task")
@CommandPermission("flash.command.schedule")
object ScheduleCommand : BaseCommand() {
    @Subcommand("list")
    fun list(sender: CommandSender?) {
    }

    @Subcommand("create|add")
    fun add(
        sender: CommandSender?,
        @Name("name") @Single name: String?,
        @Name("date (epoch)") time: Long,
        @Name("command") command: String?
    ) {
        val task = Task(name, Date(time), command)
        Flash.instance.scheduleHandler.createTask(task)
    }
}