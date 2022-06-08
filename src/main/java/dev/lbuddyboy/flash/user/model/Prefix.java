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
        Document kitUpdate = new Document("$set", prefixDocument);

        Flash.getInstance().getMongoHandler().getPrefixCollection().updateOne(query, kitUpdate, new UpdateOptions().upsert(true));

        new PrefixesUpdatePacket(Flash.getInstance().getUserHandler().getPrefixes()).send();
    }

}
