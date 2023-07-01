package dev.lbuddyboy.flash.user.model;

import com.mongodb.client.model.UpdateOptions;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.packet.PrefixesUpdatePacket;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

import javax.print.Doc;

@AllArgsConstructor
@Data
public class Prefix {

    private String id, display;
    private int weight;

    public void save() {
        Document prefixDocument = Document.parse(GSONUtils.getGSON().toJson(this, GSONUtils.PREFIX));
        prefixDocument.remove("_id");

        Document query = new Document("_id", id);
        Document prefixUpdate = new Document("$set", prefixDocument);

        Flash.getInstance().getMongoHandler().getPrefixCollection().updateOne(query, prefixUpdate, new UpdateOptions().upsert(true));
    }

    public void delete() {
        Document query = new Document("_id", id);
        Flash.getInstance().getMongoHandler().getPrefixCollection().deleteOne(query);
    }

}
