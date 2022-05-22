package dev.lbuddyboy.flash.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.util.CC;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class MongoHandler {

	MongoClient mongoClient;
	com.mongodb.client.MongoClient mongoClientURI;

	@Getter private MongoCollection<Document> userCollection;
	@Getter private MongoCollection<Document> rankCollection;
	@Getter private MongoDatabase mongoDatabase;

	public MongoHandler() {
		if (!Flash.getInstance().getConfig().getBoolean("mongo.use")) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fDid not connect to the &6Mongo Database&f &aenable&f it in the &econfig.yml&f!"));
			return;
		}

		FileConfiguration config = Flash.getInstance().getConfig();

		if (config.getBoolean("mongo.use-uri")) {

			String username = config.getString("mongo.auth.username");
			String password = config.getString("mongo.auth.password");

			String database = config.getString("mongo.database");
			String host = config.getString("mongo.host");
			int port = config.getInt("mongo.port");

			ConnectionString connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + database + "?authSource=admin");
			MongoClientSettings settings = MongoClientSettings.builder()
					.applyConnectionString(connectionString)
					.build();

			this.mongoClientURI = MongoClients.create(settings);
			this.mongoDatabase = mongoClientURI.getDatabase(database);
		} else {

			boolean auth = config.getBoolean("mongo.auth.enabled");
			String username = config.getString("mongo.auth.username");
			String password = config.getString("mongo.auth.password");

			String database = config.getString("mongo.database");
			String host = config.getString("mongo.host");
			int port = config.getInt("mongo.port");

			if (!auth) {
				this.mongoClient = new MongoClient(host, port);
			} else {
				this.mongoClient = new MongoClient(new ServerAddress(host, port), MongoCredential.createCredential(username, database, password.toCharArray()),
						MongoClientOptions.builder().build());

			}

			this.mongoDatabase = this.mongoClient.getDatabase(database);
		}

		Bukkit.getConsoleSender().sendMessage(CC.translate("&fSuccessfully connected the &6Mongo Database"));

		this.userCollection = getMongoDatabase().getCollection("Users");
		this.rankCollection = getMongoDatabase().getCollection("Ranks");
	}

}
