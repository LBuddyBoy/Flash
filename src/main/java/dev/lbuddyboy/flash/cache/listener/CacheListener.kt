package dev.lbuddyboy.flash.cache.listener

import dev.lbuddyboy.flash.Flash
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class CacheListener : Listener {
    @EventHandler
    fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        Flash.instance.cacheHandler.getUserCache().update(event.uniqueId, event.name, true)
    }
}