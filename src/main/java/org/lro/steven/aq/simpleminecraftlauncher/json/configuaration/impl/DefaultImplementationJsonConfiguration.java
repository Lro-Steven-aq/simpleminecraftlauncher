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

    /**
     * 实现/覆写接口或父类方法。
     * @param string 参数
     */
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

    /**
     * 实现/覆写接口或父类方法。
     * @param file 参数
     */
    @Override
    public void load(File file) {
        try {
            load(Files.readString(file.toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public Map<String, String> getMap() {
        return configuration;
    }
}
