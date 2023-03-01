package ru.nycistoxic.pinger.registry;

import ru.nycistoxic.pinger.Pinger;
import ru.nycistoxic.pinger.object.Server;
import ru.nycistoxic.pinger.task.ServerPingTask;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ServerRegistry {

    private final Pinger instance;
    private final Set<Server> servers;

    public ServerRegistry(Pinger instance, Set<Server> servers) {
        this.instance = instance;
        this.servers = servers;

        init();
    }

    private void init() {
        // ---- TEST ----
        servers.add(new Server("VimeWorld MiniGames", "vimeworld.net", 25565));
        servers.add(new Server("CloudCubes", "mc.cloudcubes.pro", 25565));
        servers.add(new Server("InvalidServer", "mc.invalidserver.ru", 25565));
        // --------------

        for (Server server : servers)
            instance.getExecutor().scheduleWithFixedDelay(new ServerPingTask(instance, server), 0, 10000, TimeUnit.MILLISECONDS);
    }

}
