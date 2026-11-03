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
module org.lro.steven.aq.simpleminecraftlauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.glavo.url;
    requires com.google.gson;
    requires org.jetbrains.annotations;
    requires org.apache.commons.io;
    requires okhttp3;

    // ui
    opens org.lro.steven.aq.simpleminecraftlauncher.controller.impl to javafx.fxml;
    exports org.lro.steven.aq.simpleminecraftlauncher.controller.impl to javafx.graphics;

    // for plugin-system.
    exports org.lro.steven.aq.simpleminecraftlauncher;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;
    exports org.lro.steven.aq.simpleminecraftlauncher.plugin.api.downloader;
    exports org.lro.steven.aq.simpleminecraftlauncher.json.configuaration;

    opens org.lro.steven.aq.simpleminecraftlauncher;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;
    opens org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;
    opens org.lro.steven.aq.simpleminecraftlauncher.json.configuaration;

    // for Gson
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf to com.google.gson;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings to com.google.gson;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;
    exports org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest;
    exports org.lro.steven.aq.simpleminecraftlauncher.launch;
    opens org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf to com.google.gson;
    opens org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings to com.google.gson;
}
