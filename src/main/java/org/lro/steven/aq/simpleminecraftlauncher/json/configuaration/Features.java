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
package org.lro.steven.aq.simpleminecraftlauncher.json.configuaration;

import java.util.Map;

/** 版本 JSON 中 features 规则使用的开关集合。 */
public final class Features {
    private Map<String, Boolean> map;


    /**
     * 创建 features 开关集合。
     * @param config 参数
     * @return 返回值
     */
    public static Features create(Map<String, Boolean> config) {
        Features features = new Features(config);
        return features;
    }

    /**
     * 创建 {@code Features} 实例。
     * @param config 参数
     */
    private Features(Map<String, Boolean> config) {
        this.map = config;
    }

    /**
     * 获取指定 feature 的值，缺省时返回默认值。
     * @param key 参数
     * @param defaultValue 参数
     * @return 返回值
     */
    public Boolean getOrDefault(String key, Boolean defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

}
