/*
 * Copyright (C) ${year} Lro-Steven.aq
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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