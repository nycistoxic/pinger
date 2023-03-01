package ru.nycistoxic.pinger.object;

public record PingResponse(Version version, Players players) {

    public record Version(String name, int protocol) {
    }

    public record Players(int max, int online) {
    }

}
