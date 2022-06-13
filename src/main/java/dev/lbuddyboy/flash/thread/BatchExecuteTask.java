package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.user.User;
import dev.lbuddyboy.flash.util.Batch;
import dev.lbuddyboy.flash.util.Callable;
import dev.lbuddyboy.flash.util.TimeUtils;
import dev.lbuddyboy.flash.util.bukkit.CC;
import org.bukkit.scheduler.BukkitRunnable;

public class BatchExecuteTask extends Thread {

    @Override
    public void run() {
        if (Flash.getInstance().getTransportHandler().getBatches().isEmpty()) return;
        try {
            Batch firstBatch = Flash.getInstance().getTransportHandler().getBatches().get(0);
            if (firstBatch != null) {
                for (int i = 0; i < 25; i++) {
                    try {
                        Callable callable = firstBatch.getCallbacks().remove(i);
                        if (callable == null) {
                            continue;
                        }

                        callable.call();
                    } catch (Exception ignored) {

                    }
                }
                System.out.println("Items left to execute: " + firstBatch.getCallbacks().size());
                if (firstBatch.getCallbacks().isEmpty()) {
                    long diff = System.currentTimeMillis() - firstBatch.getStartedAt();

                    firstBatch.getSender().sendMessage(CC.translate("&3" + firstBatch.getName() + " batch&f ended took &a" + TimeUtils.formatLongIntoDetailedString(diff / 1000) + "ms"));
                    firstBatch.setDone(true);
                }
            }
        } catch (Exception ignored) {

        }

        try {
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
