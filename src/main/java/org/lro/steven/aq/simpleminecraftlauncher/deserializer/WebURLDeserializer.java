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
package org.lro.steven.aq.simpleminecraftlauncher.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.io.FileUtils;
import org.glavo.url.WebURL;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WebURL 与 JSON 字符串互转。
 * <br/>
 * 反序列化时解析 URL，序列化时写回 {@code toString()}，避免 Gson 反射 WebURL 内部字段。
 */
public class WebURLDeserializer implements JsonDeserializer<WebURL>, JsonSerializer<WebURL> {

    /**
     * 实现/覆写接口或父类方法。
     * @param json 参数
     * @param typeOfT 参数
     * @param context 参数
     * @return 返回值
     * @throws JsonParseException 异常
     */
    @Override
    public WebURL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        String string = json.getAsString();
        if (FileUtils.isRegularFile(new File(string))) {
            File file = new File(string);
            return WebURL.parse(
              "file:" + file.getAbsolutePath()
            );
        }
        return WebURL.parse(string);
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param src 参数
     * @param typeOfSrc 参数
     * @param context 参数
     * @return 返回值
     */
    @Override
    public JsonElement serialize(WebURL src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
    }
}
