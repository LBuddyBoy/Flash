package dev.lbuddyboy.flash.user.model

import com.mongodb.client.model.UpdateOptions
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.gson.GSONUtils
import lombok.*
import org.bson.Document

@AllArgsConstructor
@Data
class Prefix {
    private val id: String? = null
    private val display: String? = null
    private val weight = 0
    fun save() {
        val prefixDocument = Document.parse(GSONUtils.getGSON().toJson(this, GSONUtils.PREFIX))
        prefixDocument.remove("_id")
        val query = Document("_id", id)
        val prefixUpdate = Document("\$set", prefixDocument)
        Flash.instance.mongoHandler.getPrefixCollection()
            .updateOne(query, prefixUpdate, UpdateOptions().upsert(true))
    }

    fun delete() {
        val query = Document("_id", id)
        Flash.instance.mongoHandler.getPrefixCollection().deleteOne(query)
    }
}