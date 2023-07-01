package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.schedule.Task;
import dev.lbuddyboy.flash.util.JavaUtils;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.YamlDoc;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ScheduleHandler {

    private final List<Task> tasks;
    private final Map<Task, List<Long>> messageInterval;
    private final YamlDoc config;

    public ScheduleHandler() {
        this.tasks = new ArrayList<>();
        this.messageInterval = new HashMap<>();
        this.config = new YamlDoc(Flash.getInstance().getDataFolder(), "schedule.yml");

        for (String key : this.config.gc().getConfigurationSection("schedule").getKeys(false)) {
            this.tasks.add(new Task(key, new Date(this.config.gc().getLong(key + ".date")), this.config.gc().getString(key + ".command")));
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(Flash.getInstance(), () -> {
            for (Task task : this.tasks) {
                if (task.getDate().after(new Date())) {
                    Bukkit.getScheduler().runTask(Flash.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), task.getCommand()));
                    this.tasks.remove(task);
                    this.messageInterval.remove(task);
                    this.config.gc().set("schedule." + task.getId() + ".executed", true);
                    try {
                        this.config.save();
                    } catch (IOException ignored) {}
                } else {
                    for (Long l : getReminderIntervals()) {
                        if (messageInterval.get(task) != null && messageInterval.get(task).contains(l))
                            continue;
                        if (l > task.getDate().getTime() - System.currentTimeMillis()) {

                            List<Long> newList = messageInterval.containsKey(task) ? messageInterval.get(task) : new ArrayList<>();

                            newList.add(l);

                            messageInterval.put(task, newList);

                            String timeForm = TimeUtils.formatLongIntoDetailedString(((task.getDate().getTime() - System.currentTimeMillis()) / 1000) + 1);
                            Bukkit.broadcastMessage(CC.translate("&g&l[TASKS] " + task.getId() + "&f will commence in &g" + timeForm));

                            break;
                        }
                    }
                }
            }

        }, 60, 60);

    }

    public void createTask(Task task) {
        this.getTasks().add(task);
        this.config.gc().set("schedule." + task.getId() + ".date", task.getDate().getTime());
        this.config.gc().set("schedule." + task.getId() + ".command", task.getCommand());
        try {
            this.config.save();
        } catch (IOException ignored) {}
    }

    public void delete(Task task) {
        this.getTasks().remove(task);
        this.config.gc().set("schedule." + task.getId() + ".executed", true);
        try {
            this.config.save();
        } catch (IOException ignored) {}
    }

    public List<Long> getReminderIntervals() {
        return this.config.gc().getStringList( "reminder.times").stream().map(JavaUtils::parse).collect(Collectors.toList());
    }

}
