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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** 版本 JSON 的顶层结构。 */
public record GameMetadataManifest(
        Arguments arguments,
        AssetIndex assetIndex,

        @SerializedName("assets")
        String assetsIndex,
        int complianceLevel,
        Downloads downloads,
        String id,

        @SerializedName("jar")
        String jar,
        JavaVersion javaVersion,
        List<Library> libraries,
        Logging logging,
        String mainClass,
        int minimumLauncherVersion,
        String releaseTime,
        String time,
        String type
) {
}
