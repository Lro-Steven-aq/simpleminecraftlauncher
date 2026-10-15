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

import java.util.HashMap;
import java.util.Map;

public final class Features {
    private Map<String, Boolean> map;


    public static Features create(Map<String, Boolean> config) {
        Features features = new Features(config);
        return features;
    }

    private Features(Map<String, Boolean> config) {
        this.map = config;
    }

    public Boolean getOrDefault(String key, Boolean defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

}
