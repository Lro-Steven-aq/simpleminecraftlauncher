package org.lro.steven.aq.simpleminecraftlauncher.test.plugin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.util.concurrent.CountDownLatch;


public class TestLoader {

    @Test
    public void test() throws InterruptedException {
        PluginManager manager = PluginManager.get();
        try {
            manager.startAll();
            SignalBus bus = manager.getSignalBus();
            bus.emit("log.info", LogEvent.of("Test Logger info"));
            bus.emit("log.warn", LogEvent.of("warn..."));
            bus.emit("log.error", LogEvent.of("error!"));
            bus.emit("log.fatal", LogEvent.of("FATal..."));
            // below should be ignored
            bus.emit("log.debug", LogEvent.of("debug?!"));
            bus.emit("log.trace", LogEvent.of("test...trace"));

            //
            bus.emit("ui.popup.error", PopupEvent.of("test","?error happens!" ,"Test Content"));
            bus.emit("log.error", LogEvent.of("L"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(100);
        manager.stopAll();
    }
}
