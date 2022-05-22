package dev.lbuddyboy.flash.rank.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.rank.packet.RanksUpdatePacket;
import dev.lbuddyboy.flash.util.Tasks;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.UUID;

public class MongoRank extends Rank {

    public MongoRank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;

        load();
    }

    public MongoRank(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        load();
    }

    @Override
    public void load() {
        Document document = Flash.getInstance().getMongoHandler().getRankCollection().find(Filters.eq("uuid", this.uuid.toString())).first();

        if (document == null) {
            save(true, true);
            return;
        }

        if (document.containsKey("name"))
            this.name = document.getString("name");

        if (document.containsKey("displayName"))
            this.displayName = document.getString("displayName");

        if (document.containsKey("color"))
            this.color = ChatColor.valueOf(document.getString("color"));

        if (document.containsKey("weight"))
            this.weight = document.getInteger("weight");

        if (document.containsKey("prefix"))
            this.prefix = document.getString("prefix");

        if (document.containsKey("suffix"))
            this.suffix = document.getString("suffix");

        if (document.containsKey("default"))
            this.defaultRank = document.getBoolean("default");

        if (document.containsKey("permissions"))
            this.permissions = GSONUtils.getGSON().fromJson(document.getString("permissions"), GSONUtils.STRING);

        if (document.containsKey("inheritance"))
            this.inheritance = GSONUtils.getGSON().fromJson(document.getString("inheritance"), GSONUtils.STRING);

    }

    @Override
    public void delete() {
        Flash.getInstance().getRankHandler().getRanks().remove(this.uuid);
        Flash.getInstance().getMongoHandler().getRankCollection().deleteOne(Filters.eq("uuid", this.uuid.toString()));
        new RanksUpdatePacket(Flash.getInstance().getRankHandler().getRanks()).send();
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
            document.put("displayName", this.displayName);
            document.put("color", this.color.name());
            document.put("weight", this.weight);
            document.put("prefix", this.prefix);
            document.put("suffix", this.suffix);
            document.put("permissions", GSONUtils.getGSON().toJson(this.permissions, GSONUtils.STRING));

            Flash.getInstance().getMongoHandler().getRankCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));

            if (reload) load();
            return;
        }
        Tasks.runAsync(() -> save(false, reload));
    }

}
