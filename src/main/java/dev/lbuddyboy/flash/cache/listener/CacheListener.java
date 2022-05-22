package dev.lbuddyboy.flash.cache.listener;

import dev.lbuddyboy.flash.Flash;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class CacheListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Flash.getInstance().getCacheHandler().getUserCache().update(event.getUniqueId(), event.getName());
    }

}
