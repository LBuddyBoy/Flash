package dev.lbuddyboy.flash.handler;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoDatabaseImpl;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;

@Getter
public class MongoHandler {

	MongoClient mongoClient;
	MongoClientURI mongoClientURI;

	private MongoCollection<Document> userCollection, rankCollection, cacheCollection, prefixCollection, notificationCollection;
	private MongoDatabase mongoDatabase;

	public MongoHandler() {
		if (!Flash.getInstance().getConfig().getBoolean("mongo.use")) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fDid not connect to the &6Mongo Database&f &aenable&f it in the &econfig.yml&f!"));
			return;
		}

		FileConfiguration config = Flash.getInstance().getConfig();

		if (config.getBoolean("mongo.auth.enabled")) {
			MongoCredential credential = MongoCredential.createCredential(
					config.getString("mongo.auth.username"),
					config.getString("mongo.auth.auth-db"),
					config.getString("mongo.auth.password").toCharArray()
			);

			mongoClient = new MongoClient(new ServerAddress(config.getString("mongo.host"),
					config.getInt("mongo.port")), credential, MongoClientOptions.builder().build());
		} else {
			mongoClient = new MongoClient(config.getString("mongo.host"),
					config.getInt("mongo.port"));
		}

		mongoDatabase = mongoClient.getDatabase(config.getString("mongo.database"));

		Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully connected the &6Mongo Database"));

		this.userCollection = getMongoDatabase().getCollection("Users");
		this.rankCollection = getMongoDatabase().getCollection("Ranks");
		this.cacheCollection = getMongoDatabase().getCollection("Cache");
		this.prefixCollection = getMongoDatabase().getCollection("Prefixes");
		this.notificationCollection = getMongoDatabase().getCollection("Notifications");
	}

}
