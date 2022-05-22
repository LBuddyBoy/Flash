package dev.lbuddyboy.flash.redis.sub;

import com.google.gson.Gson;
import dev.lbuddyboy.flash.redis.JedisPacket;
import dev.lbuddyboy.flash.util.gson.GSONUtils;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import redis.clients.jedis.JedisPubSub;

@NoArgsConstructor
public class JedisSubscriber extends JedisPubSub {

	@SneakyThrows
	@Override
	public void onMessage(String channel, String message) {
		Class<?> packetClass;
		int packetMessageSplit = message.indexOf("||");
		String packetClassStr = message.substring(0, packetMessageSplit);
		String messageJson = message.substring(packetMessageSplit + "||".length());
		try {
			packetClass = Class.forName(packetClassStr);
		} catch (ClassNotFoundException ignored) {
			return;
		}
		JedisPacket packet = (JedisPacket) GSONUtils.GSON.fromJson(messageJson, packetClass);
		if (packet != null) {
			packet.onReceive();
		}
	}
}

