package dev.lbuddyboy.flash.util.bukkit;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.rank.Rank;
import dev.lbuddyboy.flash.user.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CC {

	public static List<CustomColor> customColors = new ArrayList<>();
	public static final String BLUE = ChatColor.BLUE.toString();
	public static final String AQUA = ChatColor.AQUA.toString();
	public static final String YELLOW = ChatColor.YELLOW.toString();
	public static final String RED = ChatColor.RED.toString();
	public static final String GRAY = ChatColor.GRAY.toString();
	public static final String GOLD = ChatColor.GOLD.toString();
	public static final String GREEN = ChatColor.GREEN.toString();
	public static final String WHITE = ChatColor.WHITE.toString();
	public static final String BLACK = ChatColor.BLACK.toString();
	public static final String BOLD = ChatColor.BOLD.toString();
	public static final String ITALIC = ChatColor.ITALIC.toString();
	public static final String UNDER_LINE = ChatColor.UNDERLINE.toString();
	public static final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
	public static final String RESET = ChatColor.RESET.toString();
	public static final String MAGIC = ChatColor.MAGIC.toString();
	public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public static final String DARK_AQUA = ChatColor.DARK_AQUA.toString();
	public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
	public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public static final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
	public static final String DARK_RED = ChatColor.DARK_RED.toString();
	public static final String PINK = ChatColor.LIGHT_PURPLE.toString();
	public static final String UNICODE_VERTICAL_BAR = CC.GRAY + StringEscapeUtils.unescapeJava("\u2503");
	public static final String UNICODE_CAUTION = StringEscapeUtils.unescapeJava("\u26a0");
	public static final String UNICODE_ARROW_LEFT = StringEscapeUtils.unescapeJava("\u25C0");
	public static final String UNICODE_ARROW_RIGHT = StringEscapeUtils.unescapeJava("▶");
	public static final String UNICODE_ARROWS_LEFT = StringEscapeUtils.unescapeJava("\u00AB");
	public static final String UNICODE_ARROWS_RIGHT = StringEscapeUtils.unescapeJava("\u00BB");
	public static final String UNICODE_HEART = StringEscapeUtils.unescapeJava("\u2764");
	public static final String MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40);
	public static final String CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40);
	public static final String SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 16);

	private static final List<String> SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");
	public static java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<G:([0-9A-Fa-f]{6})>(.*?)</G:([0-9A-Fa-f]{6})>");

	public static String translate(String string) {
		for (CustomColor customColor : customColors) {
			string = string.replaceAll(customColor.getCode(), customColor.getColor());
		}
		return ChatColor.translateAlternateColorCodes('&', string.replaceAll("&g", "&x&0&6&9&2&f&f"));
	}

	private static String withoutSpecialChar(String source) {
		String workingString = source;
		for (String color : SPECIAL_COLORS) {
			if (workingString.contains(color)) {
				workingString = workingString.replace(color, "");
			}
		}
		return workingString;
	}

	public static String applyVictim(String message, String victim) {
		return message.replaceAll("%VICTIM%", victim);
	}

	public static String applySender(String message, String sender) {
		return message.replaceAll("%SENDER%", sender);
	}

	public static String applyPlayer(String message, UUID uuid) {
		User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
		if (user == null) {
			return CC.translate(message,
					"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
					"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
					"%PLAYER_UUID%", uuid.toString()
			);
		}
		Rank rank = user.getActiveRank();

		return CC.translate(message,
				"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
				"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
				"%PLAYER_UUID%", uuid.toString(),
				"%PLAYER_RANK_NAME%", rank.getName(),
				"%PLAYER_RANK_WEIGHT%", rank.getWeight(),
				"%PLAYER_RANK_COLOR%", rank.getColor().name(),
				"%PLAYER_RANK_DISPLAY%", rank.getDisplayName(),
				"%PLAYER_RANK_PREFIX%", rank.getPrefix(),
				"%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
				"%PLAYER_RANK_COLORED%", rank.getColoredName()
		);
	}

	public static String applyTarget(String message, UUID uuid) {
		User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
		if (user == null) {
			return CC.translate(message,
					"%TARGET%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
					"%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
					"%TARGET_UUID%", uuid.toString()
			);
		}
		Rank rank = user.getActiveRank();

		return CC.translate(message,
				"%TARGET%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
				"%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
				"%TARGET_UUID%", uuid.toString(),
				"%TARGET_RANK_NAME%", rank.getName(),
				"%TARGET_RANK_WEIGHT%", rank.getWeight(),
				"%TARGET_RANK_COLOR%", rank.getColor().name(),
				"%TARGET_RANK_DISPLAY%", rank.getDisplayName(),
				"%TARGET_RANK_PREFIX%", rank.getPrefix(),
				"%TARGET_RANK_SUFFIX%", rank.getSuffix(),
				"%TARGET_RANK_COLORED%", rank.getColoredName()
		);
	}

	public static List<String> applyTarget(List<String> message, UUID uuid) {
		User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
		if (user == null) {
			return CC.translate(message,
					"%TARGET%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
					"%TARGET_COLORED%", UserUtils.formattedName(uuid),
					"%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
					"%TARGET_UUID%", uuid.toString()
			);
		}
		Rank rank = user.getActiveRank();

		return CC.translate(message,
				"%TARGET%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
				"%TARGET_COLORED%", UserUtils.formattedName(uuid),
				"%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
				"%TARGET_UUID%", uuid.toString(),
				"%TARGET_RANK_NAME%", rank.getName(),
				"%TARGET_RANK_WEIGHT%", rank.getWeight(),
				"%TARGET_RANK_COLOR%", rank.getColor().name(),
				"%TARGET_RANK_DISPLAY%", rank.getDisplayName(),
				"%TARGET_RANK_PREFIX%", rank.getPrefix(),
				"%TARGET_RANK_SUFFIX%", rank.getSuffix(),
				"%TARGET_RANK_COLORED%", rank.getColoredName()
		);
	}

	public static List<String> applyPlayer(List<String> message, UUID uuid) {
		User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
		if (user == null) {
			return CC.translate(message,
					"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
					"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
					"%PLAYER_COLORED%", UserUtils.formattedName(uuid),
					"%PLAYER_UUID%", uuid.toString()
			);
		}
		Rank rank = user.getActiveRank();

		return CC.translate(message,
				"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
				"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
				"%PLAYER_COLORED%", UserUtils.formattedName(uuid),
				"%PLAYER_UUID%", uuid.toString(),
				"%PLAYER_RANK_NAME%", rank.getName(),
				"%PLAYER_RANK_WEIGHT%", rank.getWeight(),
				"%PLAYER_RANK_COLOR%", rank.getColor().name(),
				"%PLAYER_RANK_DISPLAY%", rank.getDisplayName(),
				"%PLAYER_RANK_PREFIX%", rank.getPrefix(),
				"%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
				"%PLAYER_RANK_COLORED%", rank.getColoredName()
		);
	}

	public static List<String> applyPlayer(List<String> message, UUID uuid, Object... objects) {
		User user = Flash.getInstance().getUserHandler().tryUser(uuid, true);
		if (user == null) {
			return CC.translate(message,
					"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
					"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
					"%PLAYER_UUID%", uuid.toString(), objects
			);
		}
		Rank rank = user.getActiveRank();

		return CC.translate(message,
				"%PLAYER%", Flash.getInstance().getCacheHandler().getUserCache().getName(uuid),
				"%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
				"%PLAYER_UUID%", uuid.toString(),
				"%PLAYER_RANK_NAME%", rank.getName(),
				"%PLAYER_RANK_WEIGHT%", rank.getWeight(),
				"%PLAYER_RANK_COLOR%", rank.getColor().name(),
				"%PLAYER_RANK_DISPLAY%", rank.getDisplayName(),
				"%PLAYER_RANK_PREFIX%", rank.getPrefix(),
				"%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
				"%PLAYER_RANK_COLORED%", rank.getColoredName(), objects
		);
	}

	public static String applyRank(String message, Rank rank) {
		return CC.translate(message,
                "%RANK_NAME%", rank.getName(),
                "%RANK_WEIGHT%", rank.getWeight(),
                "%RANK_COLOR%", rank.getColor().name(),
                "%RANK_DISPLAY%", rank.getDisplayName(),
                "%RANK_PREFIX%", rank.getPrefix(),
                "%RANK_SUFFIX%", rank.getSuffix(),
                "%RANK_COLORED%", rank.getColoredName()
                );
	}

	public static List<String> applyRank(List<String> message, Rank rank) {
		return CC.translate(message,
                "%RANK_NAME%", rank.getName(),
                "%RANK_WEIGHT%", rank.getWeight(),
                "%RANK_COLOR%", rank.getColor().name(),
                "%RANK_DISPLAY%", rank.getDisplayName(),
                "%RANK_PREFIX%", rank.getPrefix(),
                "%RANK_SUFFIX%", rank.getSuffix(),
                "%RANK_COLORED%", rank.getColoredName()
                );
	}

	public static String applyAll(String message, String sender, String victim) {
		String message2 = applySender(message, sender);
		return applyVictim(message2, victim);
	}

	public static void broadCastStaff(String message, Object... format) {
		for (Player staff : Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("flash.staff")).collect(Collectors.toList())) {
			staff.sendMessage(translate(message, format));
		}
		Bukkit.getConsoleSender().sendMessage(translate(message, format));
	}

	public static String translate(String string, Object... format) {
		for (int i = 0; i < format.length; i += 2) {
			string = string.replace((String) format[i], String.valueOf(format[i + 1]));
		}
		return translate(string);
	}

	public static List<String> translate(List<String> lore) {
		ArrayList<String> toAdd = new ArrayList();

		for (String lor : lore) {
			toAdd.add(translate(lor));
		}

		return toAdd;
	}

	public static List<String> translate(List<String> lore, Object... objects) {
		ArrayList<String> toAdd = new ArrayList();

		for (String lor : lore) {
			toAdd.add(translate(lor, objects));
		}

		return toAdd;
	}

}
