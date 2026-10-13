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
module org.lro.steven.aq.simpleminecraftlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.glavo.url;
    requires com.google.gson;
    requires org.jetbrains.annotations;

    // ui
    opens org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.fxml;
    exports org.lro.steven.aq.simpleminecraftlauncher.controller to javafx.graphics;

    // for plugin-system.
    exports org.lro.steven.aq.simpleminecraftlauncher;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

    opens org.lro.steven.aq.simpleminecraftlauncher;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game to com.google.gson;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest;
}