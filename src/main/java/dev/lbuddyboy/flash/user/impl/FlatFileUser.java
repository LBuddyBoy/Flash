package dev.lbuddyboy.flash.user.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.util.Tasks;
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
        this.online = config.getBoolean(path + "online");
        this.lastServer = config.getString(path + "lastServer");
        this.currentServer = config.getString(path + "currentServer");
        this.knownIps = config.getStringList(path + "knownIps");
        this.permissions = GSONUtils.getGSON().fromJson(config.getString(path + "permissions"), GSONUtils.USER_PERMISSION);
        this.grants = GSONUtils.getGSON().fromJson(config.getString(path + "grants"), GSONUtils.GRANT);

        if (this.grants.isEmpty()) {
            this.grants.add(Grant.defaultGrant());
        }
    }

    @Override
    public void save(boolean async) {
        save(async, false);
    }

    private void save(boolean async, boolean reload) {
        if (!async) {
            FileConfiguration config = Flash.getInstance().getUserHandler().getUsersYML().gc();
            String path = "profiles." + getUuid().toString() + ".";

            config.set(path + "uuid", this.uuid.toString());
            config.set(path + "name", this.name);
            config.set(path + "ip", this.ip);
            config.set(path + "lastServer", this.lastServer);
            config.set(path + "currentServer", this.currentServer);
            config.set(path + "online", this.online);
            config.set(path + "knownIps", this.knownIps);
            config.set(path + "permissions", GSONUtils.getGSON().toJson(this.permissions, GSONUtils.USER_PERMISSION));
            config.set(path + "grants", GSONUtils.getGSON().toJson(this.grants, GSONUtils.GRANT));

            try {
                Flash.getInstance().getUserHandler().getUsersYML().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (reload) load();
            return;
        }

        Tasks.runAsync(() -> save(false, reload));
    }
}
