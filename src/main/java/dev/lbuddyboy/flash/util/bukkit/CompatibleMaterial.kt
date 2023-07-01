package dev.lbuddyboy.flash.util.bukkit

import org.bukkit.Material

object CompatibleMaterial {
    fun getMaterial(name: String): Material {
        return try {
            Material.getMaterial(name)
        } catch (ignored: Exception) {
            if (name.equals("SKULL_ITEM", ignoreCase = true)) {
                return Material.getMaterial("PLAYER_HEAD")
            }
            if (name.equals("BARRIER", ignoreCase = true)) {
                Material.REDSTONE_BLOCK
            } else Material.getMaterial("LEGACY_$name")
        }
    }
}