package dev.lbuddyboy.flash.user.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.user.model.Grant;
import dev.lbuddyboy.flash.util.Tasks;
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

            if (document.containsKey("name"))
                this.name = document.getString("name");

            if (document.containsKey("ip"))
                this.ip = document.getString("ip");

            if (document.containsKey("currentServer"))
                this.currentServer = document.getString("currentServer");

            if (document.containsKey("lastServer"))
                this.lastServer = document.getString("lastServer");

            if (document.containsKey("punishments"))
                this.punishments = GSONUtils.getGSON().fromJson(document.getString("punishments"), GSONUtils.PUNISHMENTS);

            if (document.containsKey("permissions"))
                this.permissions = GSONUtils.getGSON().fromJson(document.getString("permissions"), GSONUtils.USER_PERMISSION);

            if (document.containsKey("knownIps"))
                this.knownIps = GSONUtils.getGSON().fromJson(document.getString("knownIps"), GSONUtils.STRING);

            if (document.containsKey("grants"))
                this.grants = GSONUtils.getGSON().fromJson(document.getString("grants"), GSONUtils.GRANT);

            if (getActiveGrants().isEmpty()) {
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
            Document document = new Document();

            document.put("uuid", this.uuid.toString());
            document.put("name", this.name);
            document.put("currentServer", this.currentServer);
            document.put("lastServer", this.lastServer);
            document.put("punishments", GSONUtils.getGSON().toJson(this.punishments, GSONUtils.PUNISHMENTS));
            document.put("permissions", GSONUtils.getGSON().toJson(this.permissions, GSONUtils.USER_PERMISSION));
            document.put("knownIps", GSONUtils.getGSON().toJson(this.knownIps, GSONUtils.STRING));
            document.put("grants", GSONUtils.getGSON().toJson(this.grants, GSONUtils.GRANT));

            Flash.getInstance().getMongoHandler().getUserCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));

            if (reload) load();
            return;
        }
        Tasks.runAsync(() -> save(false, reload));
    }

}
