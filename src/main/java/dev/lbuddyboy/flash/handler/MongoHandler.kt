package dev.lbuddyboy.flash.handler

import com.mongodb.*
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.bukkit.CC
import lombok.Getter
import org.bson.Document
import org.bukkit.Bukkit

@Getter
class MongoHandler {
    var mongoClient: MongoClient? = null
    var mongoClientURI: MongoClientURI? = null
    private val userCollection: MongoCollection<Document>
    private val rankCollection: MongoCollection<Document>
    private val cacheCollection: MongoCollection<Document>
    private val prefixCollection: MongoCollection<Document>
    private val notificationCollection: MongoCollection<Document>
    private val syncCollection: MongoCollection<Document>
    private val syncCodesCollection: MongoCollection<Document>
    private val mongoDatabase: MongoDatabase

    init {
        if (!Flash.instance.config.getBoolean("mongo.use")) {
            Bukkit.getConsoleSender()
                .sendMessage(CC.translate("&fDid not connect to the &6Mongo Database&f &aenable&f it in the &econfig.yml&f!"))
            return
        }
        val config = Flash.instance.config
        mongoClient = if (config.getBoolean("mongo.auth.enabled")) {
            val credential = MongoCredential.createCredential(
                config.getString("mongo.auth.username"),
                config.getString("mongo.auth.auth-db"),
                config.getString("mongo.auth.password").toCharArray()
            )
            MongoClient(
                ServerAddress(
                    config.getString("mongo.host"),
                    config.getInt("mongo.port")
                ), credential, MongoClientOptions.builder().build()
            )
        } else {
            MongoClient(
                config.getString("mongo.host"),
                config.getInt("mongo.port")
            )
        }
        mongoDatabase = mongoClient!!.getDatabase(config.getString("mongo.database"))
        Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully connected the &6Mongo Database"))
        userCollection = getMongoDatabase().getCollection("Users")
        rankCollection = getMongoDatabase().getCollection("Ranks")
        cacheCollection = getMongoDatabase().getCollection("Cache")
        prefixCollection = getMongoDatabase().getCollection("Prefixes")
        syncCollection = mongoDatabase.getCollection("Syncs")
        syncCodesCollection = mongoDatabase.getCollection("SyncCodes")
        notificationCollection = getMongoDatabase().getCollection("Notifications")
    }
}