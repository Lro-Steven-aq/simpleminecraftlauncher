package org.lro.steven.aq.simpleminecraftlauncher.test.plugin;

import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

public class TestPlugin {
    @Test
    public void testPlugin() {
        SignalBus signalBus = SignalBus.get();
        new TePlugin().start(signalBus);
        signalBus.emit("launcher.initialize", new TeEvent());
    }
}


class TePlugin implements Plugin {
    @Override
    public void start(SignalBus signalBus) {
        signalBus.connect("launcher.initialize", (e) -> {
            IO.println(e.source());
        });
    }
}

class TeEvent implements Event {
    @Override
    public String source() {
        return "launcher.initialization";
    }
}