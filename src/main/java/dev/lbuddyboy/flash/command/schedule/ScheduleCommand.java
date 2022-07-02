package dev.lbuddyboy.flash.command.schedule;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.schedule.Task;
import org.bukkit.command.CommandSender;

import java.util.Date;

@CommandAlias("schedule|task")
@CommandPermission("flash.command.schedule")
public class ScheduleCommand extends BaseCommand {

    @Subcommand("list")
    public static void list(CommandSender sender) {

    }

    @Subcommand("create|add")
    public static void add(CommandSender sender, @Name("name") @Single String name, @Name("date (epoch)") long time, @Name("command") String command) {

        Task task = new Task(name, new Date(time), command);
        Flash.getInstance().getScheduleHandler().createTask(task);

    }

}
