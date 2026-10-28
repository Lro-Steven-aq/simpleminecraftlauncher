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
 *
 * Plugin Exception:
 * Plugins that use the package org.lro.steven.aq.simpleminecraftlauncher.plugin.api
 * and its subpackages, together with the JAR files they call, are not required
 * to be licensed under the GPLv3 and may be distributed under any license
 * terms. See the LICENSE file ("SimpleMinecraftLauncher Plugin Exception")
 * for the full definition and conditions.
 */
package org.lro.steven.aq.simpleminecraftlauncher.test.plugin;

import org.junit.jupiter.api.Test;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;



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
            bus.emit("ui.popup.error", PopupEvent.of("test",
                    "?error happens!" ,
                    "Test Content",
                    () -> {},
                    () -> {},
                    () -> {}));
            bus.emit("log.error", LogEvent.of("L"));

            manager.stop(PluginManager.PLUGIN_PATH + "/simpleminecraftlauncher-plugin-logger-1.0-SNAPSHOT.jar");
            bus.emit("log.error", LogEvent.of("You should not see it"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(100);
        manager.stopAll();
    }
}
