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
package org.lro.steven.aq.simpleminecraftlauncher.version;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.version.impl.DefaultImplementationVersionManager;

import java.util.List;

/** 版本清单与版本安装/Java 运行时下载的接口。 */
public interface VersionManager {

    /**
     * 创建版本管理器。
     * @param configuration 启动器配置
     * @return 版本管理器
     */
    static VersionManager create(ApplicationConfiguration configuration) {
        return new DefaultImplementationVersionManager(
                "https://piston-meta.mojang.com/mc/game/version_manifest.json",
                configuration);
    }

    /**
     * Mojang 版本清单里的全部版本 id（远程）。
     */
    List<String> getVersions();

    /**
     * 本地已经安装（存在版本 JSON）的版本 id。
     */
    List<String> listAvailableVersions();

    /**
     * 获取版本清单；失败返回 null。
     */
    VersionManifest getVersionManifest();

    /**
     * 下载指定版本的 JSON、客户端 jar、依赖库和资源索引，并写入 last_version_file。
     */
    void install(String version);

    /**
     * 异步请求版本清单。
     */
    void enqueue(Callback callback);

    /**
     * 把响应体解析成版本清单。
     */
    VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response);

    /**
     * 从网络下载一个 Java 运行时（Adoptium），解压后写入 JAVA_HOME。
     */
    void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration);
}
