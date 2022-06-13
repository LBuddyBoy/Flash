package dev.lbuddyboy.flash.user.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.UUID;

public class FlatFileUser extends User {

    public FlatFileUser(UUID uuid, String name, boolean load) {
        this.uuid = uuid;
        this.name = name;

        if (load) load();
    }

    @Override
    public void load() {
        FileConfiguration config = Flash.getInstance().getUserHandler().getUsersYML().gc();

        if (!config.contains("profiles." + getUuid().toString())) {
            save(true, true);
            return;
        }

        String path = "profiles." + getUuid().toString() + ".";

        this.name = config.getString(path + "name");
        this.ip = config.getString(path + "ip");

        this.knownIps = config.getStringList(path + "knownIps");
        this.activePrefix = GSONUtils.getGSON().fromJson(config.getString(path + "activePrefix"), GSONUtils.PREFIX);
        this.blocked = GSONUtils.getGSON().fromJson(config.getString(path + "blocked"), GSONUtils.UUID);
        this.permissions = GSONUtils.getGSON().fromJson(config.getString(path + "permissions"), GSONUtils.USER_PERMISSION);
        this.punishments = GSONUtils.getGSON().fromJson(config.getString(path + "punishments"), GSONUtils.PUNISHMENTS);
        this.notes = GSONUtils.getGSON().fromJson(config.getString(path + "notes"), GSONUtils.NOTE);
        this.grants = GSONUtils.getGSON().fromJson(config.getString(path + "grants"), GSONUtils.GRANT);
        this.playerInfo = GSONUtils.getGSON().fromJson(config.getString(path + "playerInfo"), GSONUtils.PLAYER_INFO);
        this.serverInfo = GSONUtils.getGSON().fromJson(config.getString(path + "serverInfo"), GSONUtils.SERVER_INFO);
        this.staffInfo = GSONUtils.getGSON().fromJson(config.getString(path + "staffInfo"), GSONUtils.STAFF_INFO);

        if (this.grants.isEmpty()) {
            this.grants.add(Grant.defaultGrant());
        }
    }

    @Override
    public void save(boolean async) {
        save(false, false);
    }

    public void save(boolean saveConfig, boolean reload) {

        /*
        Can't run config updates async. Throws concurrent modification exceptions
         */

        FileConfiguration config = Flash.getInstance().getUserHandler().getUsersYML().gc();
        String path = "profiles." + getUuid().toString() + ".";

        config.set(path + "uuid", this.uuid.toString());
        config.set(path + "name", this.name);
        config.set(path + "ip", this.ip);

        config.set(path + "knownIps", this.knownIps);
        config.set(path + "activePrefix", GSONUtils.getGSON().toJson(this.activePrefix, GSONUtils.PREFIX));
        config.set(path + "punishments", GSONUtils.getGSON().toJson(this.punishments, GSONUtils.PUNISHMENTS));
        config.set(path + "blocked", GSONUtils.getGSON().toJson(this.blocked, GSONUtils.UUID));
        config.set(path + "permissions", GSONUtils.getGSON().toJson(this.permissions, GSONUtils.USER_PERMISSION));
        config.set(path + "notes", GSONUtils.getGSON().toJson(this.notes, GSONUtils.NOTE));
        config.set(path + "grants", GSONUtils.getGSON().toJson(this.grants, GSONUtils.GRANT));
        config.set(path + "playerInfo", GSONUtils.getGSON().toJson(this.playerInfo, GSONUtils.PLAYER_INFO));
        config.set(path + "serverInfo", GSONUtils.getGSON().toJson(this.serverInfo, GSONUtils.SERVER_INFO));
        config.set(path + "staffInfo", GSONUtils.getGSON().toJson(this.staffInfo, GSONUtils.STAFF_INFO));

        if (saveConfig) {
            try {
                Flash.getInstance().getUserHandler().getUsersYML().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (reload) load();
    }
}
