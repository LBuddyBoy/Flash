package dev.lbuddyboy.flash.server;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Data
public class Server {

    private final UUID uuid;
    private final String name;
    private String motd;
    private long lastResponse;
    private boolean whitelist;
    private int online, maxPlayers;

    public String status() {
        return !isOffline() ? whitelist ? "&eWhitelisted" : "&aOnline" : "&cOffline";
    }

    public boolean isOffline() {
        return lastResponse + 15_000L < System.currentTimeMillis();
    }

}
