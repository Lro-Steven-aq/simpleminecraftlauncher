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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/** 版本依赖库。 */
public record Library(

        @SerializedName("name")
        String mavenArtifactId,

        Downloads downloads,
        List<Rule> rules,
        List<String> natives,

        @Nullable
        Extract extract
) {

        /** 依赖库的下载信息。 */
        public record Downloads(
                @Nullable
                Artifact artifact,

                Map<String, Artifact> classifiers
        ) {
        }
}
