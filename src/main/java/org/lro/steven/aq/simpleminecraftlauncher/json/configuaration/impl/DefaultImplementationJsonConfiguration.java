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
package org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置{@code ${var}}的默认实现类.
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationJsonConfiguration implements Configuration {

    private final Map<String, String> configuration;

    /**
     * 创建一个参数配置表。
     */
    public DefaultImplementationJsonConfiguration() {
        configuration = new HashMap<>();
    }

    @Override
    public void load(String string) {
        if (string == null || string.isBlank()) {
            return;
        }
        Map<String, String> values = new Gson().fromJson(
                string, new TypeToken<Map<String, String>>() { }.getType());
        if (values != null) {
            configuration.putAll(values);
        }
    }

    @Override
    public void load(File file) {
        try {
            load(Files.readString(file.toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Map<String, String> getMap() {
        return configuration;
    }
}
