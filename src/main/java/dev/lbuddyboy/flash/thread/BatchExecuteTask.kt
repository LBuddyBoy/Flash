package dev.lbuddyboy.flash.thread

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.util.TimeUtils
import dev.lbuddyboy.flash.util.bukkit.CC

class BatchExecuteTask : Thread() {
    override fun run() {
        if (Flash.instance.transportHandler.getBatches().isEmpty()) return
        try {
            val firstBatch = Flash.instance.transportHandler.getBatches()[0]
            if (firstBatch != null) {
                for (i in 0..24) {
                    try {
                        val callable = firstBatch.callbacks.removeAt(i) ?: continue
                        callable.call()
                    } catch (ignored: Exception) {
                    }
                }
                println("Items left to execute: " + firstBatch.callbacks.size)
                if (firstBatch.callbacks.isEmpty()) {
                    val diff = System.currentTimeMillis() - firstBatch.startedAt
                    firstBatch.sender.sendMessage(
                        CC.translate(
                            "&3" + firstBatch.name + " batch&f ended took &a" + TimeUtils.formatLongIntoDetailedString(
                                diff / 1000
                            ) + "ms"
                        )
                    )
                    firstBatch.isDone = true
                }
            }
        } catch (ignored: Exception) {
        }
        try {
            sleep(500)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}