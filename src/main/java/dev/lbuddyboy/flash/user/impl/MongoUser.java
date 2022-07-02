package dev.lbuddyboy.flash.user.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.util.bukkit.Tasks;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import org.bson.Document;

import java.util.UUID;

public class MongoUser extends User {

    public MongoUser(UUID uuid, String name, boolean load) {
        this.uuid = uuid;
        this.name = name;

        if (load) load();
    }

    @Override
    public void load() {
        try {
            Document document = Flash.getInstance().getMongoHandler().getUserCollection().find(Filters.eq("uuid", this.uuid.toString())).first();

            if (document.containsKey("name")) this.name = document.getString("name");
            if (document.containsKey("ip")) this.ip = document.getString("ip");

            if (document.containsKey("blocked")) this.blocked = GSONUtils.getGSON().fromJson(document.getString("blocked"), GSONUtils.UUID);
            if (document.containsKey("activePrefix")) this.activePrefix = GSONUtils.getGSON().fromJson(document.getString("activePrefix"), GSONUtils.PREFIX);
            if (document.containsKey("punishments")) this.punishments = GSONUtils.getGSON().fromJson(document.getString("punishments"), GSONUtils.PUNISHMENTS);
            if (document.containsKey("permissions")) this.permissions = GSONUtils.getGSON().fromJson(document.getString("permissions"), GSONUtils.USER_PERMISSION);
            if (document.containsKey("knownIps")) this.knownIps = GSONUtils.getGSON().fromJson(document.getString("knownIps"), GSONUtils.STRING);
            if (document.containsKey("notes")) this.notes = GSONUtils.getGSON().fromJson(document.getString("notes"), GSONUtils.NOTE);
            if (document.containsKey("grants")) this.grants = GSONUtils.getGSON().fromJson(document.getString("grants"), GSONUtils.GRANT);
            if (document.containsKey("promotions")) this.promotions = GSONUtils.getGSON().fromJson(document.getString("promotions"), GSONUtils.PROMOTIONS);
            if (document.containsKey("demotions")) this.demotions = GSONUtils.getGSON().fromJson(document.getString("demotions"), GSONUtils.DEMOTIONS);
            if (document.containsKey("playerInfo")) this.playerInfo = GSONUtils.getGSON().fromJson(document.getString("playerInfo"), GSONUtils.PLAYER_INFO);
            if (document.containsKey("serverInfo")) this.serverInfo = GSONUtils.getGSON().fromJson(document.getString("serverInfo"), GSONUtils.SERVER_INFO);
            if (document.containsKey("staffInfo")) this.staffInfo = GSONUtils.getGSON().fromJson(document.getString("staffInfo"), GSONUtils.STAFF_INFO);

            if (getActiveGrants().isEmpty()) this.grants.add(Grant.defaultGrant());

            updateGrants();
        } catch (Exception ignored) {
            save(true, true);
        }
    }

    @Override
    public void loadRank() {
        try {
            Document document = Flash.getInstance().getMongoHandler().getUserCollection().find(Filters.eq("uuid", this.uuid.toString())).first();

            if (document.containsKey("name")) this.name = document.getString("name");
            if (document.containsKey("ip")) this.ip = document.getString("ip");

            if (document.containsKey("grants")) this.grants = GSONUtils.getGSON().fromJson(document.getString("grants"), GSONUtils.GRANT);

            if (getActiveGrants().isEmpty()) this.grants.add(Grant.defaultGrant());

            updateGrants();
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
            Document document = new Document();

            document.put("uuid", this.uuid.toString());
            document.put("name", this.name);
            document.put("ip", this.ip);

            document.put("blocked", GSONUtils.getGSON().toJson(this.blocked, GSONUtils.UUID));
            document.put("punishments", GSONUtils.getGSON().toJson(this.punishments, GSONUtils.PUNISHMENTS));
            document.put("permissions", GSONUtils.getGSON().toJson(this.permissions, GSONUtils.USER_PERMISSION));
            document.put("knownIps", GSONUtils.getGSON().toJson(this.knownIps, GSONUtils.STRING));
            document.put("notes", GSONUtils.getGSON().toJson(this.notes, GSONUtils.NOTE));
            document.put("grants", GSONUtils.getGSON().toJson(this.grants, GSONUtils.GRANT));
            document.put("promotions", GSONUtils.getGSON().toJson(this.promotions, GSONUtils.PROMOTIONS));
            document.put("demotions", GSONUtils.getGSON().toJson(this.demotions, GSONUtils.DEMOTIONS));
            document.put("activePrefix", GSONUtils.getGSON().toJson(this.activePrefix, GSONUtils.PREFIX));
            document.put("playerInfo", GSONUtils.getGSON().toJson(this.playerInfo, GSONUtils.PLAYER_INFO));
            document.put("serverInfo", GSONUtils.getGSON().toJson(this.serverInfo, GSONUtils.SERVER_INFO));
            document.put("staffInfo", GSONUtils.getGSON().toJson(this.staffInfo, GSONUtils.STAFF_INFO));

            Flash.getInstance().getMongoHandler().getUserCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));

            if (reload) load();
            return;
        }
        Tasks.runAsync(() -> save(false, reload));
    }

}
