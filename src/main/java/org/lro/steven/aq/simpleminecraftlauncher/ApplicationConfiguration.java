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
package org.lro.steven.aq.simpleminecraftlauncher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfigurationMapper;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class ApplicationConfiguration implements Configuration {

    private ApplicationConfigurationMapper configurations;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .create();
    private static final SignalBus signalBus = PluginManager.get().getSignalBus();

    public ApplicationConfiguration() {
        this.configurations = null;
    }

    @Override
    public void load(String content) {
        content = content.strip();
        // parse it.
        configurations = GSON.fromJson(content, ApplicationConfigurationMapper.class);
        check(configurations);
    }

    @Override
    public void load(File file) {
        initializeConfigurationFileIfAbsent(file);
//        byte[] buffer = IOUtils.byteArray();
//        try (InputStream inputStream = IOUtils.buffer(new FileInputStream(file))) {
//            IOUtils.readFully(inputStream, buffer);
//        } catch (FileNotFoundException e) {
//            signalBus.emit("log.error",
//                    LogEvent.of("Can't find launcher configuration file: %s; we will use default configurations".formatted(file)));
//        } catch (IOException e) {
//            signalBus.emit("log.error",
//                    LogEvent.of("IO Exception, at reading file: %s; we will use default configurations".formatted(file)));
//        }
//        load(new String(buffer));

        String content = null;
        try {
            content = Files.readString(file.toPath());
        } catch (IOException e) {
            signalBus.emit("log.error",
                    LogEvent.of("IO Exception, at reading file: %s; we will use default configurations".formatted(file)));
        }

        load(content);

    }

    public void initializeConfigurationFileIfAbsent(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
                file.setReadable(true);
                file.setWritable(true);
                Files.writeString(file.toPath(), "{" +
                        "\"configuration_version\": \"1.0\"," +
                        "\"settings\": {" +
                        "}" +
                        "}");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void check(ApplicationConfigurationMapper configurations) {
        if (configurations == null) {
            throw new IllegalArgumentException("Null Configurations");
        }
        if (configurations.configurationVersion() == null) {
            signalBus.emit("log.error", LogEvent.of("Can't find the version of configurations"));
        }
        if (configurations.userSettings() == null) {
            signalBus.emit("log.warn", LogEvent.of("Can't find the user's settings."));
        }
    }

    @Override
    public Map<String, String> getMap() {
        return GSON.fromJson(GSON.toJson(configurations), Map.class);
    }

    public ApplicationConfigurationMapper getConfigurations() {
        return configurations;
    }
}
