package dev.lbuddyboy.flash.util

import dev.lbuddyboy.flash.FlashLanguage
import org.bukkit.entity.Player
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object NameMCUtils {
    fun hasLiked(player: Player): Boolean {
        var voted = false
        try {
            val url =
                URL("https://api.namemc.com/server/" + FlashLanguage.SERVER_IP.string + "/likes?profile=" + player.uniqueId.toString())
            val urlConnection = url.openConnection()
            val inputStream = urlConnection.getInputStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = null
            while (bufferedReader.readLine().also { line = it } != null) {
                voted = java.lang.Boolean.parseBoolean(line)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return voted
    }
}