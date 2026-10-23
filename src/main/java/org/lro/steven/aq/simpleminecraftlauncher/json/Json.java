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
package org.lro.steven.aq.simpleminecraftlauncher.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.ArgumentDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Argument;

/**
 * 项目内共用的 Gson 实例：统一注册 WebURL 与版本参数的自定义反序列化器。
 */
public final class Json {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .registerTypeAdapter(Argument.class, new ArgumentDeserializer())
            .create();

    private Json() {
    }
}
