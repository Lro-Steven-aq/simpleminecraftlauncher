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
package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl.DefaultImplementationJsonConfiguration;

import java.util.Map;

/**
 * 对于json解析的配置.
 * <br/>
 * 其实就是一一对应.
 * 比如：${classpath}<br>
 * 其本质就是一个 Map，这是在内部维护的。
 * @author Lro.Steven.aq
 * @since 0.1
 */
public interface Configuration {
    static Configuration create() {
        return new DefaultImplementationJsonConfiguration();
    }

    /**
     * 获取Configuration内部的表.
     * <br>
     * 这是它的本质。<br>
     * @return 其内部维护的表。
     */
    Map<String, String> getMap();

    /**
     * 向内部的Map存储一对K, V.
     * @param key 键
     * @param value 值
     */
    default void add(String key, String value) {
        getMap().put(key, value);
    }

    /**
     * 从内部的Map依据一个K，获取一个 V.
     * @param key 键
     * @return 值
     */
    default String get(String key) {
        return getMap().get(key);
    }

    /**
     * 从内部的Map依据一个K，获取一个 V.
     * @param key 键
     * @return 值
     */
    default String get(String key, String defaultValue) {
        return getMap().getOrDefault(key, defaultValue);
    }
}
