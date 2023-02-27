package ru.nycistoxic.pinger.object;

import lombok.Data;

public record PingResponse(Version version, Players players) {

    @Data
    public class Version {

        private final String name;
        private final int protocol;

    }

    @Data
    public class Players {

        private final int max;
        private final int online;

    }

}
