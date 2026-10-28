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
package org.lro.steven.aq.simpleminecraftlauncher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

/** 负责读取、校验并保存启动器配置。 */
public class ApplicationConfigurationLoader {

    public static final String DEFAULT_PATH = ".simpleminecraftlauncher/configurations.json";

    private ApplicationConfiguration configurations;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .create();
    private static final SignalBus signalBus = PluginManager.get().getSignalBus();

    /**
     * 创建一个配置加载器。
     */
    public ApplicationConfigurationLoader() {
        this.configurations = null;
    }

    /**
     * 从 JSON 字符串或文件加载并校验配置。
     * @param content 参数
     */
    public void load(String content) {
        if (content == null || content.isBlank()) {
            content = "{\"configuration_version\":\"1.0\",\"settings\":{}}";
        }
        content = content.strip();
        // parse it.
        configurations = GSON.fromJson(content, ApplicationConfiguration.class);
        check(configurations);
    }

    /**
     * 把当前配置写回默认配置文件。
     */
    public void save() {
        configurations.save(DEFAULT_PATH);
    }

    /**
     * 从 JSON 字符串或文件加载并校验配置。
     * @param file 参数
     */
    public void load(File file) {
        initializeConfigurationFileIfAbsent(file);
        load(readContent(file));
    }

    private String readContent(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            signalBus.emit("log.error",
                    LogEvent.of("IO Exception, at reading file: %s; we will use default configurations".formatted(file)));
            return null;
        }
    }

    /**
     * 如果配置文件不存在，创建一个最小默认配置。
     * @param file 参数
     */
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

    private void check(ApplicationConfiguration configurations) {
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

    /**
     * 把当前配置转换成键值表。
     * @return 返回值
     */
    public Map<String, String> getMap() {
        return GSON.fromJson(GSON.toJson(configurations), Map.class);
    }

    /**
     * 返回当前配置对象。
     * @return 返回值
     */
    public ApplicationConfiguration getConfigurations() {
        return configurations;
    }
}
