package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TipsMessageTask extends BukkitRunnable {

    private int last;

    @Override
    public void run() {
        List<String> tips = FlashLanguage.TIPS_LIST.getStringList();
        String tip = tips.get(last++);

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate(tip)));

        if (last >= tips.size()) last = 0;
    }
}
