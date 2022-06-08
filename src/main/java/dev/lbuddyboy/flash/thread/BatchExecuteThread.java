package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.util.Batch;
import dev.lbuddyboy.flash.util.bukkit.CC;
import dev.lbuddyboy.flash.util.Callable;
import dev.lbuddyboy.flash.util.TimeUtils;

public class BatchExecuteThread extends Thread {

    @Override
    public void run() {
        while (true) {
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
                Thread.sleep(2_000);
            } catch (InterruptedException ignored) {}

        }

    }
}
