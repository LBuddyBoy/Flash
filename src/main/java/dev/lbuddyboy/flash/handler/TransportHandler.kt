package dev.lbuddyboy.flash.handler

import dev.lbuddyboy.flash.Flash
import dev.lbuddyboy.flash.transfer.LuckPermsTransport
import dev.lbuddyboy.flash.util.Batch
import lombok.Getter
import java.util.stream.Collectors

@Getter
class TransportHandler {
    private val batches: MutableList<Batch>
    private var luckPermsTransport: LuckPermsTransport? = null

    init {
        batches = ArrayList()
        if (Flash.instance.server.pluginManager.getPlugin("LuckPerms") != null) {
            luckPermsTransport = LuckPermsTransport()
        }
    }

    fun getBatches(): List<Batch> {
        return batches.stream().filter { batch: Batch -> !batch.isDone }.collect(Collectors.toList())
    }

    fun addBatch(batch: Batch) {
        batches.add(batch)
    }
}