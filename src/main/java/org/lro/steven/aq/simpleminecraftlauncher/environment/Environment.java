package org.lro.steven.aq.simpleminecraftlauncher.environment;

import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

public final class Environment {
    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();

    private Environment() {}

    public static void scanJavaRuntime(ApplicationConfiguration configuration) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Scanning for Java Home.."));
        // todo
    }

}
