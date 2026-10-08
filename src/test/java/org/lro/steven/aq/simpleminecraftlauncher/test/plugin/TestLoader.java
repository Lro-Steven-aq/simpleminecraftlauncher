package org.lro.steven.aq.simpleminecraftlauncher.test.plugin;

import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class TestLoader {
    @Test
    public void test() {
        PluginManager manager = PluginManager.get();
        try {
            manager.startAll();
            manager.getSignalBus().emit("log.info", LogEvent.of("Test Logger"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        manager.stopAll();
    }
}
