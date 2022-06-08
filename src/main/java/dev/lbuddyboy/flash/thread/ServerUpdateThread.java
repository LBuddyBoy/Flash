package dev.lbuddyboy.flash.thread;

import dev.lbuddyboy.flash.Flash;
import dev.lbuddyboy.flash.server.Server;
import dev.lbuddyboy.flash.user.User;

public class ServerUpdateThread extends Thread {

    @Override
    public void run() {
        while (true) {

            Server server = Flash.getInstance().getServerHandler().getCurrentServer();

            Flash.getInstance().getServerHandler().update(server);

            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException ignored) {}

        }
    }
}
