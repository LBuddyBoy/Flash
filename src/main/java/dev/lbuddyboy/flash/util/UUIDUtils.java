package dev.lbuddyboy.flash.util;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;

import java.util.UUID;

public class UUIDUtils {

    public static String formattedName(UUID uuid) {
        try {
            User user = Flash.getInstance().getUserHandler().getUser(uuid, true);

            if (user == null) {
                return "&4Console";
            }

            return user.getColoredName();
        } catch (Exception ignored) {
            return "&4Console";
        }
    }

}
