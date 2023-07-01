package dev.lbuddyboy.flash.user.model

import lombok.Data
import lombok.RequiredArgsConstructor
import org.bukkit.inventory.ItemStack

@RequiredArgsConstructor
@Data
class ServerInfo {
    private val server: String? = null
    private val contents = arrayOfNulls<ItemStack>(36)
    private val armor = arrayOfNulls<ItemStack>(4)
}