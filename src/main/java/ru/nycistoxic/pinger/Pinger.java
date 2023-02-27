package ru.nycistoxic.pinger;

import com.google.gson.Gson;

import lombok.Getter;

import ru.nycistoxic.pinger.registry.ServerRegistry;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Getter
public class Pinger {

    @Getter
    private static Pinger instance;
    private final Gson gson;
    private final ScheduledExecutorService executor;
    private final ServerRegistry serverRegistry;

    private final ConsoleHandler handler = new ConsoleHandler();
    private final Logger logger = Logger.getLogger("Pinger"); {
        handler.setFormatter(new LogFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    public Pinger() {
        instance = this;
        gson = new Gson();
        executor = Executors.newScheduledThreadPool(3);
        serverRegistry = new ServerRegistry();

        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
    }

    public static void main(String[] args) {
        new Pinger();
    }

    private class LogFormatter extends Formatter {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            String message = dateFormat.format(new Date(record.getMillis())) + " [" + record.getLevel().getName() + "] " + record.getMessage();

            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                message += writer.toString();
            }

            message += "\n\r";
            return message;
        }

    }

}
