package dev.lbuddyboy.flash.handler;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.transfer.LuckPermsTransport;
import dev.lbuddyboy.flash.util.Batch;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TransportHandler {

    private final List<Batch> batches;
    private LuckPermsTransport luckPermsTransport;

    public TransportHandler() {
        this.batches = new ArrayList<>();

        if (Flash.getInstance().getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            this.luckPermsTransport = new LuckPermsTransport();
        }

    }

    public List<Batch> getBatches() {
        return this.batches.stream().filter(batch -> !batch.isDone()).collect(Collectors.toList());
    }

    public void addBatch(Batch batch) {
        this.batches.add(batch);
    }

}
