package dev.lbuddyboy.flash.rank.impl;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.handler.RedisHandler;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.gson.GSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisRank extends Rank {

    public RedisRank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;

        load();
    }

    public RedisRank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        load();
    }

    @Override
    public void load() {
        try {
            RedisRank rank = GSONUtils.getGSON().fromJson(RedisHandler.requestJedis().getResource().hget("Ranks", getUuid().toString()), GSONUtils.REDIS_RANK);

            this.name = rank.getName();
            this.uuid = rank.getUuid();
            this.displayName = rank.getDisplayName();
            this.color = rank.getColor();
            this.weight = rank.getWeight();
            this.prefix = rank.getPrefix();
            this.defaultRank = rank.isDefaultRank();
            this.suffix = rank.getSuffix();
            this.permissions = rank.getPermissions();
            this.inheritance = rank.getInheritance();

        } catch (Exception ignored) {
            save(true, true);
        }
    }

    @Override
    public void delete() {
        Flash.getInstance().getRankHandler().getRanks().remove(this.uuid);
        RedisHandler.requestJedis().getResource().hdel("Ranks", getUuid().toString());
        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();
    }

    @Override
    public void save(boolean async) {
        save(async, false);
    }

    private void save(boolean async, boolean reload) {
        RedisHandler.requestJedis().getResource().hset("Ranks", getUuid().toString(), toJson());
        if (reload) load();
    }

    public String toJson() {
        return GSONUtils.getGSON().toJson(this, GSONUtils.REDIS_RANK);
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
