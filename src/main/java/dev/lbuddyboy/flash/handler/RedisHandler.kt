package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.redis.sub.JedisSubscriber;
import dev.lbuddyboy.flash.util.bukkit.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisHandler {

	@Getter private static boolean enabled;

	public RedisHandler() {
		if (!Flash.getInstance().getConfig().getBoolean("redis.use")) {
			Bukkit.getConsoleSender().sendMessage(CC.translate("&fDid not connect to the &bRedis Database&f &aenable&f it in the &econfig.yml&f!"));
			return;
		}
		connect();
	}

	public static void connect() {
		JedisPool connectTo = requestJedis();

		Thread thread = new Thread(() -> {
			while (true) {
				try {
					Jedis jedis = connectTo.getResource();
					Throwable throwable = null;
					try {
						JedisSubscriber pubSub = new JedisSubscriber();
						jedis.subscribe(pubSub, "Flash:Global");
					} catch (Throwable pubSub) {
						throwable = pubSub;
						throw pubSub;
					} finally {
						if (jedis == null) continue;
						if (throwable != null) {
							try {
								jedis.close();
							} catch (Throwable pubSub) {
								throwable.addSuppressed(pubSub);
							}
							continue;
						}
						jedis.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Redis Packet Subscriber");
		thread.setDaemon(true);
		thread.start();

		enabled = true;
	}

	public static JedisPool requestJedis() {
		return new JedisPool(new JedisPoolConfig(),
				Flash.getInstance().getConfig().getString("redis.host"), Flash.getInstance().getConfig().getInt("redis.port"), 20000,
				(Flash.getInstance().getConfig().getString("redis.auth.password").isEmpty() ? null : Flash.getInstance().getConfig().getString("redis.auth.password")),
				Flash.getInstance().getConfig().getInt("redis.channel-id", 0));
	}

}
