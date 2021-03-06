package dev.lbuddyboy.flash.user.impl;

import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.gson.GSONUtils;

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
            this.permissions = user.getPermissions();
            this.knownIps = user.getKnownIps();
            this.punishments.addAll(user.getPunishments());
            this.notes = user.getNotes();
            this.grants = user.getGrants();
            this.promotions = user.getPromotions();
            this.demotions = user.getDemotions();
            this.activePrefix = user.getActivePrefix();
            this.playerInfo = user.getPlayerInfo();
            this.staffInfo = user.getStaffInfo();

            if (this.grants.isEmpty()) {
                this.grants.add(Grant.defaultGrant());
            }

            updateGrants();
        } catch (Exception ignored) {
            save(true, true);
        }

    }

    @Override
    public void loadRank() {
        load();
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
