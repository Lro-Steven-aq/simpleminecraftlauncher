package org.lro.steven.aq.simpleminecraftlauncher.test.plugin;

import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;


public class TestLoader {
    @Test
    public void test() {
        PluginManager manager = PluginManager.get();
        try {
            manager.startAll();
            SignalBus bus = manager.getSignalBus();
            bus.emit("log.info", LogEvent.of("Test Logger info"));
            bus.emit("log.warn", LogEvent.of("warn..."));
            bus.emit("log.fatal", LogEvent.of("FATal..."));
            // below should be ignored
            bus.emit("log.debug", LogEvent.of("debug?!"));
            bus.emit("log.trace", LogEvent.of("test...trace"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        manager.stopAll();
    }
}
