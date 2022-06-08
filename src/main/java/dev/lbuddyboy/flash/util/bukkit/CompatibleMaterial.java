package dev.lbuddyboy.flash.util.bukkit;

import org.bukkit.Material;

public class CompatibleMaterial {

    public static Material getMaterial(String name) {
        try {
            return Material.getMaterial(name);
        } catch (Exception ignored) {
            if (name.equalsIgnoreCase("PLAYER_HEAD")) {
                return Material.SKULL_ITEM;
            }
            if (name.equalsIgnoreCase("BARRIER")) {
                return Material.REDSTONE_BLOCK;
            }
            return Material.getMaterial("LEGACY_" + name);
        }
    }

}
