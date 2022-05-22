package dev.lbuddyboy.flash.user.impl;

import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.user.model.UserPermission;
import dev.lbuddyboy.flash.util.Tasks;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisUser extends User {

    public RedisUser(UUID uuid, String name, boolean load) {
        this.uuid = uuid;
        this.name = name;

        if (load) load();
    }

    @Override
    public void load() {
        try {
            RedisUser user = GSONUtils.getGSON().fromJson(RedisHandler.requestJedis().getResource().hget("Users", getUuid().toString()), GSONUtils.REDIS_USER);

            this.name = user.getName();
            this.ip = user.getIp();
            this.lastServer = user.getLastServer();
            this.permissions = user.getPermissions();
            this.knownIps = user.getKnownIps();
            this.grants = user.getGrants();

            if (this.grants.isEmpty()) {
                this.grants.add(Grant.defaultGrant());
            }
        } catch (Exception ignored) {
            save(true, true);
        }
    }

    @Override
    public void save(boolean async) {
        save(async, false);
    }

    private void save(boolean async, boolean reload) {
        if (!async) {
            RedisHandler.requestJedis().getResource().hset("Users", getUuid().toString(), toJson());
            if (reload) load();
            return;
        }
        Tasks.runAsync(() -> save(false, reload));
    }

    public String toJson() {
        return GSONUtils.getGSON().toJson(this, GSONUtils.REDIS_USER);
    }

}
