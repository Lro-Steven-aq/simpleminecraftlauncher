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
import org.jetbrains.annotations.Nullable;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;

import java.util.List;

/** 版本依赖库。 */
public record Library(

        @SerializedName("name")
        String mavenArtifactId,

        Downloads downloads,
        List<Rule> rules,
        Natives natives,

        @Nullable
        Extract extract
) {

        /** 依赖库的下载信息。 */
        public record Downloads(
                @Nullable
                Artifact artifact,

                Classifiers classifiers
        ) {
        }

        /** 依赖库的原生库分类器，按操作系统区分。 */
        public record Classifiers(
                @SerializedName("natives-linux") @Nullable Artifact linux,
                @SerializedName("natives-osx") @Nullable Artifact osx,
                @SerializedName("natives-windows") @Nullable Artifact windows
        ) {
        }

        /** 依赖库的原生库名称，按操作系统区分。 */
        public record Natives(
                @SerializedName("linux") String linux,
                @SerializedName("osx") String osx,
                @SerializedName("windows") String windows
        ) {
                /**
                 * 返回指定操作系统对应的原生库名称。
                 * @param os 操作系统
                 * @return 原生库名称；没有时返回 null
                 */
                public String forOs(OperatingSystem os) {
                        return switch (os) {
                                case LINUX -> linux;
                                case OSX -> osx;
                                case WINDOWS -> windows;
                                case null -> null;
                        };
                }
        }
}
