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

import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Library;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.RuleEvaluator;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 根据版本 JSON 和用户配置生成启动参数表（供 Parser 替换 ${...}）。
 */
public final class LaunchConfiguration {

    private LaunchConfiguration() {
    }

    /**
     * 根据版本 JSON 和用户配置构建启动参数表。
     * @param configuration 参数
     * @param manifest 参数
     * @param gameDirectory 参数
     * @param features 参数
     * @return 返回值
     */
    public static Configuration create(ApplicationConfiguration configuration,
                                       GameMetadataManifest manifest,
                                       Path gameDirectory,
                                       Features features) {
        UserSettings settings = configuration == null ? null : configuration.userSettings();
        String username = username(settings);
        Path versionDirectory = GameDirectory.versionDirectory(gameDirectory, manifest.id());
        Path clientJar = clientJar(manifest, versionDirectory);
        Configuration result = Configuration.create();
        addAuth(result, username);
        addPaths(result, manifest, gameDirectory, versionDirectory);
        result.add("classpath", classpath(gameDirectory, manifest, clientJar, features));
        return result;
    }

    private static String username(UserSettings settings) {
        return settings != null && settings.username() != null && !settings.username().isBlank()
                ? settings.username()
                : "Player";
    }

    private static Path clientJar(GameMetadataManifest manifest, Path versionDirectory) {
        String jarName = manifest.jar() != null && !manifest.jar().isBlank() ? manifest.jar() : manifest.id();
        return versionDirectory.resolve(jarName + ".jar");
    }

    private static void addAuth(Configuration result, String username) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
        result.add("auth_player_name", username);
        result.add("auth_uuid", uuid.toString());
        result.add("auth_access_token", "0");
        result.add("clientid", "");
        result.add("auth_xuid", "");
        result.add("user_type", "legacy");
    }

    private static void addPaths(Configuration result,
                                 GameMetadataManifest manifest,
                                 Path gameDirectory,
                                 Path versionDirectory) {
        result.add("version_name", manifest.id());
        result.add("game_directory", gameDirectory.toString());
        result.add("assets_root", GameDirectory.assetsDirectory(gameDirectory).toString());
        result.add("assets_index_name", assetIndex(manifest));
        result.add("version_type", manifest.type() == null ? "release" : manifest.type());
        result.add("natives_directory", GameDirectory.nativesDirectory(versionDirectory).toString());
        result.add("launcher_name", "SimpleMinecraftLauncher");
        result.add("launcher_version", "1.0");
        result.add("resolution_width", "854");
        result.add("resolution_height", "480");
    }

    /**
     * 构建客户端与依赖库的 classpath。
     * @param gameDirectory 参数
     * @param manifest 参数
     * @param clientJar 参数
     * @param features 参数
     * @return 返回值
     */
    public static String classpath(Path gameDirectory,
                                   GameMetadataManifest manifest,
                                   Path clientJar,
                                   Features features) {
        List<String> entries = new ArrayList<>();
        addClientJar(entries, clientJar);
        addLibraries(entries, gameDirectory, manifest, features);
        return String.join(File.pathSeparator, entries);
    }

    private static void addClientJar(List<String> entries, Path clientJar) {
        if (clientJar != null && Files.isRegularFile(clientJar)) {
            entries.add(clientJar.toString());
        }
    }

    private static void addLibraries(List<String> entries,
                                     Path gameDirectory,
                                     GameMetadataManifest manifest,
                                     Features features) {
        if (manifest.libraries() == null) {
            return;
        }
        Path libraries = GameDirectory.librariesDirectory(gameDirectory);
        for (Library library : manifest.libraries()) {
            addLibrary(entries, library, libraries, features);
        }
    }

    private static void addLibrary(List<String> entries, Library library, Path libraries, Features features) {
        if (library == null || library.mavenArtifactId() == null) {
            return;
        }
        if (!RuleEvaluator.isAllowed(library.rules(), features)) {
            return;
        }
        String path = artifactPath(library);
        if (path != null) {
            addLibraryJar(entries, libraries.resolve(path));
        }
    }

    private static void addLibraryJar(List<String> entries, Path jar) {
        if (Files.isRegularFile(jar)) {
            entries.add(jar.toString());
        } else {
            PluginManager.get().getSignalBus().emit("log.warn", LogEvent.of("Missing library: " + jar));
        }
    }

    private static String assetIndex(GameMetadataManifest manifest) {
        if (manifest.assetIndex() != null && manifest.assetIndex().id() != null) {
            return manifest.assetIndex().id();
        }
        return manifest.assetsIndex();
    }

    /**
     * 依赖库 jar 的相对路径：优先使用 downloads.artifact.path，否则从 Maven 坐标推导。
     */
    public static String artifactPath(Library library) {
        String path = artifactPathFromDownloads(library);
        return path != null ? path : artifactPathFromCoordinates(library.mavenArtifactId());
    }

    private static String artifactPathFromDownloads(Library library) {
        if (library.downloads() != null
                && library.downloads().artifact() != null
                && library.downloads().artifact().path() != null) {
            return library.downloads().artifact().path();
        }
        return null;
    }

    private static String artifactPathFromCoordinates(String mavenArtifactId) {
        if (mavenArtifactId == null) {
            return null;
        }
        String[] parts = mavenArtifactId.split(":");
        if (parts.length < 3) {
            return null;
        }
        String group = parts[0].replace('.', '/');
        String artifact = parts[1];
        String version = parts[2];
        String classifier = parts.length > 3 && !parts[3].isBlank() ? "-" + parts[3] : "";
        return group + "/" + artifact + "/" + version + "/" + artifact + "-" + version + classifier + ".jar";
    }
}
