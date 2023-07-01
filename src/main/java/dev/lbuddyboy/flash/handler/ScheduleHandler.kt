package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.schedule.Task
import dev.lbuddyboy.flash.util.JavaUtils
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.YamlDoc
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.Getter
import org.bukkit.Bukkit
import java.io.IOException
import java.util.*
import java.util.stream.Collectors

@Getter
class ScheduleHandler {
    private val tasks: MutableList<Task>
    private val messageInterval: MutableMap<Task, MutableList<Long>?>
    private val config: YamlDoc

    init {
        tasks = ArrayList()
        messageInterval = HashMap()
        config = YamlDoc(Flash.instance.dataFolder, "schedule.yml")
        for (key in config.gc().getConfigurationSection("schedule").getKeys(false)) {
            tasks.add(
                Task(
                    key, Date(config.gc().getLong("$key.date")), config.gc().getString(
                        "$key.command"
                    )
                )
            )
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(Flash.instance, {
            for (task in tasks) {
                if (task.date.after(Date())) {
                    Bukkit.getScheduler().runTask(Flash.instance) {
                        Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            task.command
                        )
                    }
                    tasks.remove(task)
                    messageInterval.remove(task)
                    config.gc()["schedule." + task.id + ".executed"] = true
                    try {
                        config.save()
                    } catch (ignored: IOException) {
                    }
                } else {
                    for (l in reminderIntervals) {
                        if (messageInterval[task] != null && messageInterval[task]!!.contains(l)) continue
                        if (l > task.date.time - System.currentTimeMillis()) {
                            val newList = if (messageInterval.containsKey(task)) messageInterval[task] else ArrayList()
                            newList!!.add(l)
                            messageInterval[task] = newList
                            val timeForm =
                                TimeUtils.formatLongIntoDetailedString((task.date.time - System.currentTimeMillis()) / 1000 + 1)
                            Bukkit.broadcastMessage(CC.translate("&g&l[TASKS] " + task.id + "&f will commence in &g" + timeForm))
                            break
                        }
                    }
                }
            }
        }, 60, 60)
    }

    fun createTask(task: Task) {
        this.getTasks().add(task)
        config.gc()["schedule." + task.id + ".date"] = task.date.time
        config.gc()["schedule." + task.id + ".command"] = task.command
        try {
            config.save()
        } catch (ignored: IOException) {
        }
    }

    fun delete(task: Task) {
        this.getTasks().remove(task)
        config.gc()["schedule." + task.id + ".executed"] = true
        try {
            config.save()
        } catch (ignored: IOException) {
        }
    }

    val reminderIntervals: List<Long>
        get() = config.gc().getStringList("reminder.times").stream().map { input: String? -> JavaUtils.parse(input) }
            .collect(Collectors.toList())
}