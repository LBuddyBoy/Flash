package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.FlashLanguage;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.Batch;
import dev.lbuddyboy.flash.util.Callable;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class TipsMessageThread extends Thread {

    private int last;

    @Override
    public void run() {
        while (true) {

            List<String> tips = FlashLanguage.TIPS_LIST.getStringList();
            String tip = tips.get(last++);

            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate(tip)));

            if (last >= tips.size()) last = 0;

            try {
                Thread.sleep(FlashLanguage.TIPS_DELAY_SECONDS.getInt() * 1000L);
            } catch (InterruptedException ignored) {}

        }
    }
}
