package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import lombok.Getter;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEvent implements Event {
    private static Logger logger = null;
    private final String message;
    private LogEvent(String message) {
        this.message = message;
    }

    public static Event of(String message) {
        return new LogEvent(message);
    }
    public Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger("org.lro.steven.aq.simpleminecraftlauncher.plugin");
        }
        return logger;
    }

    @Override
    public String source() {
        return "test logger";
    }

    public String getMessage() {
        return message;
    }
}
