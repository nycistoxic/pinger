package ru.nycistoxic.pinger.object;

import lombok.Data;

@Data
public class Server {

    private final String name;
    private final String ip;
    private final int port;

}
