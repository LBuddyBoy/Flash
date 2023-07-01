package dev.lbuddyboy.flash.util.bukkit

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.rank.Rank
import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.*
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

object CC {
    var customColors: MutableList<CustomColor?> = ArrayList()
    val BLUE = ChatColor.BLUE.toString()
    val AQUA = ChatColor.AQUA.toString()
    val YELLOW = ChatColor.YELLOW.toString()
    val RED = ChatColor.RED.toString()
    val GRAY = ChatColor.GRAY.toString()
    val GOLD = ChatColor.GOLD.toString()
    val GREEN = ChatColor.GREEN.toString()
    val WHITE = ChatColor.WHITE.toString()
    val BLACK = ChatColor.BLACK.toString()
    val BOLD = ChatColor.BOLD.toString()
    val ITALIC = ChatColor.ITALIC.toString()
    val UNDER_LINE = ChatColor.UNDERLINE.toString()
    val STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString()
    val RESET = ChatColor.RESET.toString()
    val MAGIC = ChatColor.MAGIC.toString()
    val DARK_BLUE = ChatColor.DARK_BLUE.toString()
    val DARK_AQUA = ChatColor.DARK_AQUA.toString()
    val DARK_GRAY = ChatColor.DARK_GRAY.toString()
    val DARK_GREEN = ChatColor.DARK_GREEN.toString()
    val DARK_PURPLE = ChatColor.DARK_PURPLE.toString()
    val DARK_RED = ChatColor.DARK_RED.toString()
    val PINK = ChatColor.LIGHT_PURPLE.toString()
    val UNICODE_VERTICAL_BAR = GRAY + StringEscapeUtils.unescapeJava("\u2503")
    val UNICODE_CAUTION = StringEscapeUtils.unescapeJava("\u26a0")
    val UNICODE_ARROW_LEFT = StringEscapeUtils.unescapeJava("\u25C0")
    val UNICODE_ARROW_RIGHT = StringEscapeUtils.unescapeJava("▶")
    val UNICODE_ARROWS_LEFT = StringEscapeUtils.unescapeJava("\u00AB")
    val UNICODE_ARROWS_RIGHT = StringEscapeUtils.unescapeJava("\u00BB")
    val UNICODE_HEART = StringEscapeUtils.unescapeJava("\u2764")
    val MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40)
    val CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 40)
    val SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 16)
    private val SPECIAL_COLORS: List<String> = mutableListOf("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m")
    var pattern = Pattern.compile("<G:([0-9A-Fa-f]{6})>(.*?)</G:([0-9A-Fa-f]{6})>")
    fun translate(string: String?): String {
        var string = string
        for (customColor in customColors) {
            string = string.replace(customColor.getCode().toRegex(), customColor.getColor())
        }
        return ChatColor.translateAlternateColorCodes('&', string!!.replace("&g".toRegex(), "&x&0&6&9&2&f&f"))
    }

    private fun withoutSpecialChar(source: String): String {
        var workingString = source
        for (color in SPECIAL_COLORS) {
            if (workingString.contains(color)) {
                workingString = workingString.replace(color, "")
            }
        }
        return workingString
    }

    fun applyVictim(message: String, victim: String?): String {
        return message.replace("%VICTIM%".toRegex(), victim!!)
    }

    fun applySender(message: String, sender: String?): String {
        return message.replace("%SENDER%".toRegex(), sender!!)
    }

    fun applyPlayer(message: String?, uuid: UUID?): String {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
            ?: return translate(
                message,
                "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
                "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid)!!,
                "%PLAYER_UUID%", uuid.toString()
            )
        val rank = user.activeRank
        return translate(
            message,
            "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
            "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid)!!,
            "%PLAYER_UUID%", uuid.toString(),
            "%PLAYER_RANK_NAME%", rank.getName(),
            "%PLAYER_RANK_WEIGHT%", rank.getWeight(),
            "%PLAYER_RANK_COLOR%", rank.getColor().name,
            "%PLAYER_RANK_DISPLAY%", rank!!.getDisplayName()!!,
            "%PLAYER_RANK_PREFIX%", rank.getPrefix(),
            "%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
            "%PLAYER_RANK_COLORED%", rank.coloredName
        )
    }

    fun applyTarget(message: String?, uuid: UUID?): String {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
            ?: return translate(
                message,
                "%TARGET%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
                "%TARGET_DISPLAY%", UserUtils.formattedName(uuid)!!,
                "%TARGET_UUID%", uuid.toString()
            )
        val rank = user.activeRank
        return translate(
            message,
            "%TARGET%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
            "%TARGET_DISPLAY%", UserUtils.formattedName(uuid)!!,
            "%TARGET_UUID%", uuid.toString(),
            "%TARGET_RANK_NAME%", rank.getName(),
            "%TARGET_RANK_WEIGHT%", rank.getWeight(),
            "%TARGET_RANK_COLOR%", rank.getColor().name,
            "%TARGET_RANK_DISPLAY%", rank!!.getDisplayName()!!,
            "%TARGET_RANK_PREFIX%", rank.getPrefix(),
            "%TARGET_RANK_SUFFIX%", rank.getSuffix(),
            "%TARGET_RANK_COLORED%", rank.coloredName
        )
    }

    fun applyTarget(message: List<String?>?, uuid: UUID?): List<String?> {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
            ?: return translate(
                message,
                "%TARGET%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
                "%TARGET_COLORED%", UserUtils.formattedName(uuid),
                "%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
                "%TARGET_UUID%", uuid.toString()
            )
        val rank = user.activeRank
        return translate(
            message,
            "%TARGET%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
            "%TARGET_COLORED%", UserUtils.formattedName(uuid),
            "%TARGET_DISPLAY%", UserUtils.formattedName(uuid),
            "%TARGET_UUID%", uuid.toString(),
            "%TARGET_RANK_NAME%", rank.getName(),
            "%TARGET_RANK_WEIGHT%", rank.getWeight(),
            "%TARGET_RANK_COLOR%", rank.getColor().name,
            "%TARGET_RANK_DISPLAY%", rank!!.getDisplayName(),
            "%TARGET_RANK_PREFIX%", rank.getPrefix(),
            "%TARGET_RANK_SUFFIX%", rank.getSuffix(),
            "%TARGET_RANK_COLORED%", rank.coloredName
        )
    }

    fun applyPlayer(message: List<String?>?, uuid: UUID?): List<String?> {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
            ?: return translate(
                message,
                "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
                "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
                "%PLAYER_COLORED%", UserUtils.formattedName(uuid),
                "%PLAYER_UUID%", uuid.toString()
            )
        val rank = user.activeRank
        return translate(
            message,
            "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
            "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
            "%PLAYER_COLORED%", UserUtils.formattedName(uuid),
            "%PLAYER_UUID%", uuid.toString(),
            "%PLAYER_RANK_NAME%", rank.getName(),
            "%PLAYER_RANK_WEIGHT%", rank.getWeight(),
            "%PLAYER_RANK_COLOR%", rank.getColor().name,
            "%PLAYER_RANK_DISPLAY%", rank!!.getDisplayName(),
            "%PLAYER_RANK_PREFIX%", rank.getPrefix(),
            "%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
            "%PLAYER_RANK_COLORED%", rank.coloredName
        )
    }

    fun applyPlayer(message: List<String?>?, uuid: UUID, vararg objects: Any?): List<String?> {
        val user = Flash.instance.userHandler.tryUser(uuid, true)
            ?: return translate(
                message,
                "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
                "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
                "%PLAYER_UUID%", uuid.toString(), objects
            )
        val rank = user.activeRank
        return translate(
            message,
            "%PLAYER%", Flash.instance.cacheHandler.getUserCache().getName(uuid),
            "%PLAYER_DISPLAY%", UserUtils.formattedName(uuid),
            "%PLAYER_UUID%", uuid.toString(),
            "%PLAYER_RANK_NAME%", rank.getName(),
            "%PLAYER_RANK_WEIGHT%", rank.getWeight(),
            "%PLAYER_RANK_COLOR%", rank.getColor().name,
            "%PLAYER_RANK_DISPLAY%", rank!!.getDisplayName(),
            "%PLAYER_RANK_PREFIX%", rank.getPrefix(),
            "%PLAYER_RANK_SUFFIX%", rank.getSuffix(),
            "%PLAYER_RANK_COLORED%", rank.coloredName, objects
        )
    }

    fun applyRank(message: String?, rank: Rank?): String {
        return translate(
            message,
            "%RANK_NAME%", rank.getName(),
            "%RANK_WEIGHT%", rank.getWeight(),
            "%RANK_COLOR%", rank.getColor().name,
            "%RANK_DISPLAY%", rank!!.getDisplayName()!!,
            "%RANK_PREFIX%", rank.getPrefix(),
            "%RANK_SUFFIX%", rank.getSuffix(),
            "%RANK_COLORED%", rank.coloredName
        )
    }

    fun applyRank(message: List<String?>?, rank: Rank?): List<String?> {
        return translate(
            message,
            "%RANK_NAME%", rank.getName(),
            "%RANK_WEIGHT%", rank.getWeight(),
            "%RANK_COLOR%", rank.getColor().name,
            "%RANK_DISPLAY%", rank!!.getDisplayName(),
            "%RANK_PREFIX%", rank.getPrefix(),
            "%RANK_SUFFIX%", rank.getSuffix(),
            "%RANK_COLORED%", rank.coloredName
        )
    }

    fun applyAll(message: String, sender: String?, victim: String?): String {
        val message2 = applySender(message, sender)
        return applyVictim(message2, victim)
    }

    fun broadCastStaff(message: String?, vararg format: Any?) {
        for (staff in Bukkit.getOnlinePlayers().stream()
            .filter { player: Player -> player.hasPermission("flash.staff") }
            .collect(Collectors.toList())) {
            staff.sendMessage(translate(message, *format))
        }
        Bukkit.getConsoleSender().sendMessage(translate(message, *format))
    }

    fun translate(string: String?, vararg format: Any): String {
        var string = string
        var i = 0
        while (i < format.size) {
            string = string!!.replace((format[i] as String), format[i + 1].toString())
            i += 2
        }
        return translate(string)
    }

    fun translate(lore: List<String?>?): List<String?> {
        val toAdd: ArrayList<String?> = ArrayList<Any?>()
        for (lor in lore!!) {
            toAdd.add(translate(lor))
        }
        return toAdd
    }

    fun translate(lore: List<String?>?, vararg objects: Any?): List<String?> {
        val toAdd: ArrayList<String?> = ArrayList<Any?>()
        for (lor in lore!!) {
            toAdd.add(translate(lor, *objects))
        }
        return toAdd
    }
}