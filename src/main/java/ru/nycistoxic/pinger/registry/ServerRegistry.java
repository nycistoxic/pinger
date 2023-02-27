package ru.nycistoxic.pinger.registry;

import ru.nycistoxic.pinger.Pinger;
import ru.nycistoxic.pinger.object.Server;
import ru.nycistoxic.pinger.task.ServerPingTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ServerRegistry {

    private final Pinger instance = Pinger.getInstance();
    private final Set<Server> servers = new HashSet<>();

    public ServerRegistry() {
        init();
    }

    private void init() {
        // ---- TEST ----
        servers.add(new Server("VimeWorld MiniGames", "vimeworld.net", 25565));
        servers.add(new Server("CloudCubes", "mc.cloudcubes.pro", 25565));
        servers.add(new Server("InvalidServer", "mc.invalidserver.ru", 25565));
        // --------------

        for (Server server : servers)
            instance.getExecutor().scheduleWithFixedDelay(new ServerPingTask(server), 0, 10000, TimeUnit.MILLISECONDS);
    }

}
