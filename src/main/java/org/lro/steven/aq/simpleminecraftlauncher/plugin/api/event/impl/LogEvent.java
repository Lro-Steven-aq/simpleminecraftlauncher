package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

public class LogEvent implements Event {
    private final String message;
    private LogEvent(String message) {
        this.message = message;
    }

    public static Event of(String message) {
        return new LogEvent(message);
    }

    @Override
    public String source() {
        return "logger event";
    }

    public String getMessage() {
        return message;
    }
}
