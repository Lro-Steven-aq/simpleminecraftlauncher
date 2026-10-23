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
package org.lro.steven.aq.simpleminecraftlauncher.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Argument;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Rule;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 版本的 arguments 数组里每一项既可能是字符串，也可能是带 rules 的对象。
 * Gson 默认无法把这两种形式都映射到 {@link Argument}，这里做统一处理。
 */
public class ArgumentDeserializer implements JsonDeserializer<Argument> {

    private static final Type RULE_LIST = new TypeToken<List<Rule>>() { }.getType();
    private static final Type STRING_LIST = new TypeToken<List<String>>() { }.getType();

    @Override
    public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        if (json.isJsonPrimitive()) {
            return new Argument(null, List.of(json.getAsString()));
        }
        if (!json.isJsonObject()) {
            throw new JsonParseException("Invalid argument: " + json);
        }
        return fromObject(json.getAsJsonObject(), context);
    }

    private Argument fromObject(JsonObject object, JsonDeserializationContext context) {
        List<Rule> rules = object.has("rules")
                ? context.deserialize(object.get("rules"), RULE_LIST)
                : null;
        List<String> value = object.has("value")
                ? context.deserialize(object.get("value"), STRING_LIST)
                : List.of();
        return new Argument(rules, value);
    }
}
