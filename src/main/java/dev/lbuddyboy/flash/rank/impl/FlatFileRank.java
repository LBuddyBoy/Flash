package dev.lbuddyboy.flash.rank.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlatFileRank extends Rank {

    public FlatFileRank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;

        load();
    }

    public FlatFileRank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        load();
    }

    @Override
    public void load() {
        FileConfiguration config = Flash.getInstance().getRankHandler().getRanksYML().gc();

        if (!config.contains("ranks." + getUuid().toString())) {
            save(true, true);
            return;
        }

        String path = "ranks." + getUuid().toString() + ".";

        this.name = config.getString(path + "name");
        this.displayName = config.getString(path + "displayName");
        this.color = ChatColor.valueOf(config.getString(path + "color"));
        this.weight = config.getInt(path + "weight");
        this.defaultRank = config.getBoolean(path + "default");
        this.prefix = config.getString(path + "prefix");
        this.suffix = config.getString(path + "suffix");
        this.permissions = config.getStringList(path + "permissions");
        this.inheritance = config.getStringList(path + "inheritance");

    }

    @Override
    public void delete() {
        // No clue how to delete shit from a yaml file
    }

    @Override
    public void save(boolean async) {
        save(async, false);
    }

    private void save(boolean async, boolean reload) {
        FileConfiguration config = Flash.getInstance().getRankHandler().getRanksYML().gc();
        String path = "ranks." + getUuid().toString() + ".";

        config.set(path + "uuid", this.uuid.toString());
        config.set(path + "name", this.name);
        config.set(path + "displayName", this.displayName);
        config.set(path + "color", this.color.name());
        config.set(path + "weight", this.weight);
        config.set(path + "default", this.defaultRank);
        config.set(path + "prefix", this.prefix);
        config.set(path + "suffix", this.suffix);
        config.set(path + "permissions", this.permissions);
        config.set(path + "inheritance", this.inheritance);

        try {
            Flash.getInstance().getRankHandler().getRanksYML().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (reload) load();
    }

    @Override
    public List<UUID> getUsersWithRank() {
        List<UUID> peopleWithThisRank = new ArrayList<>();

        for (UUID uuid : Flash.getInstance().getCacheHandler().getUserCache().allUUIDs()) {
            User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);

            if (user == null) continue;

            if (user.getActiveRank() != null && user.getActiveRank().getUuid().toString().equalsIgnoreCase(this.uuid.toString())) peopleWithThisRank.add(uuid);
        }

        return peopleWithThisRank;
    }
}
