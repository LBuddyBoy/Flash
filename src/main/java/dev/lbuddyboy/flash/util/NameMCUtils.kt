package dev.lbuddyboy.flash.util;

import dev.lbuddyboy.flash.FlashLanguage;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NameMCUtils {

    public static boolean hasLiked(Player player) {
        boolean voted = false;
        try {
            URL url = new URL("https://api.namemc.com/server/" + FlashLanguage.SERVER_IP.getString() + "/likes?profile=" + player.getUniqueId().toString());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                voted = Boolean.parseBoolean(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return voted;
    }
}
