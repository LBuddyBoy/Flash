package dev.lbuddyboy.flash.util.bukkit

import org.bukkit.inventory.ItemStack

object ItemUtils {
    fun setDisplayName(stack: ItemStack, name: String?) {
        val meta = stack.itemMeta
        meta.displayName = name
        stack.itemMeta = meta
    }
}