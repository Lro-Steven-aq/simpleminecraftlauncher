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
package org.lro.steven.aq.simpleminecraftlauncher.launch;

import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 游戏目录/版本目录/依赖/资源/ natives 目录的解析。
 */
public final class GameDirectory {

    /**
     * 创建 {@code GameDirectory} 实例。
     */
    private GameDirectory() {
    }

    /**
     * 用户配置里的 game_directory，缺省为 ~/.minecraft。
     */
    public static Path resolve(ApplicationConfiguration configuration) {
        String configured = null;
        if (configuration != null) {
            UserSettings settings = configuration.userSettings();
            if (settings != null) {
                configured = settings.gameDirectory();
            }
        }
        if (configured != null && !configured.isBlank()) {
            return Path.of(configured);
        }
        return Path.of(System.getProperty("user.home"), ".minecraft");
    }

    /**
     * 返回指定版本的目录。
     * @param gameDirectory 参数
     * @param versionId 参数
     * @return 返回值
     */
    public static Path versionDirectory(Path gameDirectory, String versionId) {
        return gameDirectory.resolve("versions").resolve(versionId);
    }

    /**
     * 返回依赖库目录。
     * @param gameDirectory 参数
     * @return 返回值
     */
    public static Path librariesDirectory(Path gameDirectory) {
        return gameDirectory.resolve("libraries");
    }

    /**
     * 返回资源目录。
     * @param gameDirectory 参数
     * @return 返回值
     */
    public static Path assetsDirectory(Path gameDirectory) {
        return gameDirectory.resolve("assets");
    }

    /**
     * 找到版本目录下的 natives 目录。
     * 优先使用 "natives-&lt;os&gt;-&lt;arch&gt;"，其次 "natives-&lt;os&gt;"、"natives"。
     */
    public static Path nativesDirectory(Path versionDirectory) {
        String os = osName();
        String arch = normalizeArch(System.getProperty("os.arch", "x86_64"));
        Path directory = firstExisting(versionDirectory, os, arch);
        return directory != null ? directory : versionDirectory;
    }

    /**
     * 内部方法：os Name。
     * @return 返回值
     */
    private static String osName() {
        return switch (OperatingSystem.getOs()) {
            case WINDOWS -> "windows";
            case OSX -> "macos";
            case LINUX -> "linux";
            case null -> "linux";
        };
    }

    /**
     * 内部方法：first Existing。
     * @param versionDirectory 参数
     * @param os 参数
     * @param arch 参数
     * @return 返回值
     */
    private static Path firstExisting(Path versionDirectory, String os, String arch) {
        for (String name : java.util.List.of("natives-" + os + "-" + arch, "natives-" + os, "natives")) {
            Path candidate = versionDirectory.resolve(name);
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * 把 JVM 架构名规范化为 natives 目录使用的名字。
     * @param arch 参数
     * @return 返回值
     */
    public static String normalizeArch(String arch) {
        return switch (arch) {
            case "amd64", "x86_64" -> "x86_64";
            case "aarch64", "arm64" -> "arm64";
            case "x86", "i386", "i686" -> "x86";
            default -> arch;
        };
    }
}
