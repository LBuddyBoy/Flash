package dev.lbuddyboy.flash.util.bukkit

import com.google.common.collect.ImmutableMap
import lombok.experimental.UtilityClass
import org.bukkit.ChatColor
import org.bukkit.DyeColor

@UtilityClass
object ColorUtil {
    // Credits: https://github.com/IPVP-MC/iBase/blob/master/base/src/main/java/com/doctordark/util/BukkitUtils.java
    val COLOR_MAP: ImmutableMap<ChatColor?, DyeColor?>? = null
    val COLOR_GLASS_MAP: ImmutableMap<ChatColor, Int>? = null
    val COLOR_NAME_MAP: ImmutableMap<ChatColor, String>? = null

    init {
        COLOR_MAP = ImmutableMap.builder<ChatColor?, DyeColor?>().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE)
            .put(ChatColor.BLACK, DyeColor.BLACK).put(
            ChatColor.BLUE, DyeColor.LIGHT_BLUE
        ).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(
            ChatColor.DARK_GRAY, DyeColor.GRAY
        ).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.MAGENTA).put(
            ChatColor.DARK_RED, DyeColor.RED
        ).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(
            ChatColor.GREEN, DyeColor.LIME
        ).put(ChatColor.LIGHT_PURPLE, DyeColor.PINK).put(ChatColor.RED, DyeColor.RED).put(
            ChatColor.WHITE, DyeColor.WHITE
        ).put(ChatColor.YELLOW, DyeColor.YELLOW).build()
        COLOR_GLASS_MAP = ImmutableMap.builder<ChatColor, Int>().put(ChatColor.AQUA, 3).put(ChatColor.BLACK, 15).put(
            ChatColor.BLUE, 11
        ).put(ChatColor.DARK_AQUA, 9).put(ChatColor.DARK_BLUE, 11).put(ChatColor.DARK_GRAY, 8).put(
            ChatColor.DARK_GREEN, 13
        ).put(ChatColor.DARK_PURPLE, 10).put(ChatColor.DARK_RED, 14).put(ChatColor.GOLD, 1).put(
            ChatColor.GRAY, 7
        ).put(ChatColor.GREEN, 5).put(ChatColor.LIGHT_PURPLE, 2).put(ChatColor.RED, 14).put(
            ChatColor.WHITE, 0
        ).put(ChatColor.YELLOW, 4).build()
        COLOR_NAME_MAP =
            ImmutableMap.builder<ChatColor, String>().put(ChatColor.AQUA, "Aqua").put(ChatColor.BLACK, "Black").put(
                ChatColor.BLUE, "Blue"
            ).put(ChatColor.DARK_AQUA, "Cyan").put(ChatColor.DARK_BLUE, "Dark Blue")
                .put(ChatColor.DARK_GRAY, "Dark Gray").put(
                ChatColor.DARK_GREEN, "Dark Green"
            ).put(ChatColor.DARK_PURPLE, "Purple").put(ChatColor.DARK_RED, "Dark Red").put(
                ChatColor.GOLD, "Gold"
            ).put(ChatColor.GRAY, "Gray").put(ChatColor.GREEN, "Green").put(ChatColor.LIGHT_PURPLE, "Pink").put(
                ChatColor.RED, "Red"
            ).put(ChatColor.WHITE, "White").put(ChatColor.YELLOW, "Yellow").build()
    }
}