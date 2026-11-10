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
package org.lro.steven.aq.simpleminecraftlauncher.version.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.glavo.url.WebURL;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;
import org.lro.steven.aq.simpleminecraftlauncher.json.Json;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.launch.GameDirectory;
import org.lro.steven.aq.simpleminecraftlauncher.launch.LaunchConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Library;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.RuleEvaluator;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.Version;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.DownloadEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;
import org.lro.steven.aq.simpleminecraftlauncher.version.VersionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * VersionManager 的默认实现：从 Mojang 版本清单下载并安装版本。
 */
public class DefaultImplementationVersionManager implements VersionManager {

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final int JAVA_RUNTIME_FEATURE = 25;
    private static final String ASSET_BASE_URL = "https://resources.download.minecraft.net/";

    private final WebURL versionManifestUrl;
    private final ApplicationConfiguration configuration;
    private volatile VersionManifest cachedManifest;

    /**
     * 创建版本管理器。
     * @param url 参数
     * @param configuration 参数
     */
    public DefaultImplementationVersionManager(String url, ApplicationConfiguration configuration) {
        this.versionManifestUrl = WebURL.parse(url);
        this.configuration = configuration;
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public List<String> getVersions() {
        VersionManifest manifest = getVersionManifest();
        if (manifest == null || manifest.versions() == null) {
            return List.of();
        }
        return manifest.versions().stream()
                .filter(Objects::nonNull)
                .map(Version::id)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public List<String> listAvailableVersions() {
        Path versionsDirectory = GameDirectory.resolve(configuration).resolve("versions");
        if (!Files.isDirectory(versionsDirectory)) {
            return List.of();
        }
        try (var stream = Files.list(versionsDirectory)) {
            return stream.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(id -> Files.isRegularFile(versionsDirectory.resolve(id).resolve(id + ".json")))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't list local versions: " + e.getMessage()));
            return List.of();
        }
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public VersionManifest getVersionManifest() {
        if (cachedManifest != null) {
            return cachedManifest;
        }
        DownloadEvent event = DownloadEvent.of(versionManifestUrl.toString());
        PluginManager.get().getSignalBus().emit("download.read", event);
        if (!event.handled()) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Downloader plugin is not loaded"));
            return null;
        }
        if (event.error() != null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't download version manifest: " + event.error().getMessage()));
            return null;
        }
        try {
            cachedManifest = Json.GSON.fromJson(event.content(), VersionManifest.class);
            return cachedManifest;
        } catch (RuntimeException e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't download version manifest: " + e.getMessage()));
            return null;
        }
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param version 参数
     */
    @Override
    public void install(String version) {
        if (!canInstall(version)) {
            return;
        }
        Version selected = findVersion(version);
        if (selected == null) {
            return;
        }
        Path gameDirectory = GameDirectory.resolve(configuration);
        Path versionDirectory = GameDirectory.versionDirectory(gameDirectory, version);
        Path jsonFile = versionDirectory.resolve(version + ".json");
        installFiles(selected, gameDirectory, versionDirectory, version, jsonFile);
    }

    /**
     * 安装 Files。
     * @param selected 参数
     * @param gameDirectory 参数
     * @param versionDirectory 参数
     * @param version 参数
     * @param jsonFile 参数
     */
    private void installFiles(Version selected, Path gameDirectory, Path versionDirectory, String version, Path jsonFile) {
        try {
            downloadVersionJson(selected, versionDirectory, jsonFile);
            installVersionFiles(gameDirectory, versionDirectory, version, jsonFile);
            saveInstalledVersion(jsonFile);
            logInstalled(version);
        } catch (IOException | RuntimeException e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Install failed: " + e.getMessage()));
        }
    }

    /**
     * 内部方法：log Installed。
     * @param version 参数
     */
    private static void logInstalled(String version) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Version installed: " + version));
    }

    /**
     * 判断是否 Install。
     * @param version 参数
     * @return 返回值
     */
    private boolean canInstall(String version) {
        if (version == null || version.isBlank()) {
            return false;
        }
        if (configuration != null && configuration.userSettings() != null) {
            return true;
        }
        SIGNAL_BUS.emit("log.error", LogEvent.of("Can't install version: user settings are missing"));
        return false;
    }

    /**
     * 查找 Version。
     * @param version 参数
     * @return 返回值
     */
    private Version findVersion(String version) {
        VersionManifest manifest = getVersionManifest();
        if (manifest == null || manifest.versions() == null) {
            return null;
        }
        Version selected = manifest.versions().stream()
                .filter(Objects::nonNull)
                .filter(entry -> version.equals(entry.id()))
                .findFirst()
                .orElse(null);
        if (selected == null || selected.url() == null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Version not found in manifest: " + version));
            return null;
        }
        return selected;
    }

    /**
     * 下载 Version Json。
     * @param selected 参数
     * @param versionDirectory 参数
     * @param jsonFile 参数
     * @throws IOException 异常
     */
    private void downloadVersionJson(Version selected, Path versionDirectory, Path jsonFile) throws IOException {
        Files.createDirectories(versionDirectory);
        SIGNAL_BUS.emit("log.info", LogEvent.of("Downloading version JSON: " + selected.id()));
        download(selected.url().toString(), jsonFile);
    }

    /**
     * 安装 Version Files。
     * @param gameDirectory 参数
     * @param versionDirectory 参数
     * @param version 参数
     * @param jsonFile 参数
     * @throws IOException 异常
     */
    private void installVersionFiles(Path gameDirectory, Path versionDirectory, String version, Path jsonFile) throws IOException {
        GameMetadataManifest metadata = Json.GSON.fromJson(Files.readString(jsonFile), GameMetadataManifest.class);
        downloadClientJar(versionDirectory, version, metadata);
        downloadLibraries(gameDirectory, metadata);
        downloadAssetIndex(gameDirectory, metadata);
        downloadAssetObjects(gameDirectory, metadata);
    }

    /**
     * 保存 Installed Version。
     * @param jsonFile 参数
     */
    private void saveInstalledVersion(Path jsonFile) {
        configuration.userSettings().lastVersionJsonFile(jsonFile.toString());
        configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
    }

    /**
     * 下载 Client Jar。
     * @param versionDirectory 参数
     * @param version 参数
     * @param metadata 参数
     * @throws IOException 异常
     */
    private void downloadClientJar(Path versionDirectory, String version, GameMetadataManifest metadata) throws IOException {
        if (metadata == null || metadata.downloads() == null || metadata.downloads().client() == null
                || metadata.downloads().client().url() == null) {
            return;
        }
        String jarName = metadata.jar() != null && !metadata.jar().isBlank() ? metadata.jar() : version;
        download(metadata.downloads().client().url().toString(), versionDirectory.resolve(jarName + ".jar"));
    }

    /**
     * 下载 Libraries。
     * @param gameDirectory 参数
     * @param metadata 参数
     */
    private void downloadLibraries(Path gameDirectory, GameMetadataManifest metadata) {
        if (metadata == null || metadata.libraries() == null) {
            return;
        }
        Path libraries = GameDirectory.librariesDirectory(gameDirectory);
        Features features = Features.create(Map.of());
        int downloaded = 0;
        for (Library library : metadata.libraries()) {
            if (downloadLibrary(library, libraries, features)) {
                downloaded++;
            }
        }
        if (downloaded > 0) {
            SIGNAL_BUS.emit("log.info", LogEvent.of("Libraries ready: " + downloaded));
        }
    }

    /**
     * 下载 Library。
     * @param library 参数
     * @param libraries 参数
     * @param features 参数
     * @return 返回值
     */
    private boolean downloadLibrary(Library library, Path libraries, Features features) {
        if (library == null || library.mavenArtifactId() == null) {
            return false;
        }
        if (!RuleEvaluator.isAllowed(library.rules(), features)) {
            return false;
        }
        String path = LaunchConfiguration.artifactPath(library);
        String url = artifactUrl(library);
        if (path == null || url == null) {
            return false;
        }
        return downloadLibraryJar(url, libraries.resolve(path), path);
    }

    /**
     * 下载 Library Jar。
     * @param url 参数
     * @param target 参数
     * @param path 参数
     * @return 返回值
     */
    private boolean downloadLibraryJar(String url, Path target, String path) {
        try {
            download(url, target);
            return true;
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't download library %s: %s".formatted(path, e.getMessage())));
            return false;
        }
    }

    /**
     * 内部方法：artifact Url。
     * @param library 参数
     * @return 返回值
     */
    private String artifactUrl(Library library) {
        if (library.downloads() == null || library.downloads().artifact() == null
                || library.downloads().artifact().url() == null) {
            return null;
        }
        return library.downloads().artifact().url().toString();
    }

    /**
     * 下载 Asset Index。
     * @param gameDirectory 参数
     * @param metadata 参数
     */
    private void downloadAssetIndex(Path gameDirectory, GameMetadataManifest metadata) {
        if (metadata == null || metadata.assetIndex() == null
                || metadata.assetIndex().url() == null || metadata.assetIndex().id() == null) {
            return;
        }
        Path target = GameDirectory.assetsDirectory(gameDirectory)
                .resolve("indexes")
                .resolve(metadata.assetIndex().id() + ".json");
        try {
            download(metadata.assetIndex().url().toString(), target);
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't download asset index: " + e.getMessage()));
        }
    }

    /**
     * 下载 Asset Objects。
     * @param gameDirectory 参数
     * @param metadata 参数
     */
    private void downloadAssetObjects(Path gameDirectory, GameMetadataManifest metadata) {
        if (metadata == null || metadata.assetIndex() == null || metadata.assetIndex().id() == null) {
            return;
        }
        Path indexFile = GameDirectory.assetsDirectory(gameDirectory)
                .resolve("indexes")
                .resolve(metadata.assetIndex().id() + ".json");
        if (!Files.isRegularFile(indexFile)) {
            return;
        }
        try {
            JsonObject root = JsonParser.parseString(Files.readString(indexFile)).getAsJsonObject();
            JsonObject objects = root.getAsJsonObject("objects");
            if (objects != null && !objects.isEmpty()) {
                downloadObjects(GameDirectory.assetsDirectory(gameDirectory).resolve("objects"), objects);
            }
        } catch (IOException | RuntimeException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't read asset index: " + e.getMessage()));
        }
    }

    /**
     * 下载 Objects。
     * @param objectsDirectory 参数
     * @param objects 参数
     */
    private void downloadObjects(Path objectsDirectory, JsonObject objects) {
        Set<String> hashes = new LinkedHashSet<>();
        for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
            JsonObject object = entry.getValue().getAsJsonObject();
            if (object.has("hash")) {
                hashes.add(object.get("hash").getAsString());
            }
        }
        AtomicInteger completed = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (String hash : hashes) {
            executor.submit(() -> downloadAssetObject(objectsDirectory, hash, completed, failed, hashes.size()));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        SIGNAL_BUS.emit("log.info", LogEvent.of("Assets ready: %d/%d (failed %d)"
                .formatted(completed.get(), hashes.size(), failed.get())));
    }

    /**
     * 下载 Asset Object。
     * @param objectsDirectory 参数
     * @param hash 参数
     * @param completed 参数
     * @param failed 参数
     * @param total 参数
     */
    private void downloadAssetObject(Path objectsDirectory, String hash,
                                     AtomicInteger completed, AtomicInteger failed, int total) {
        try {
            Path target = objectsDirectory.resolve(hash.substring(0, 2)).resolve(hash);
            if (!Files.isRegularFile(target)) {
                download(ASSET_BASE_URL + hash.substring(0, 2) + "/" + hash, target);
            }
            int count = completed.incrementAndGet();
            if (count % 500 == 0) {
                SIGNAL_BUS.emit("log.info", LogEvent.of("Assets: " + count + "/" + total));
            }
        } catch (IOException | RuntimeException e) {
            failed.incrementAndGet();
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't download asset %s: %s".formatted(hash, e.getMessage())));
        }
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param callback 参数
     */
    @Override
    public void enqueue(Callback callback) {
        Request request = new Request.Builder().url(versionManifestUrl.toString()).build();
        HTTP_CLIENT.newCall(request).enqueue(callback);
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param call 参数
     * @param response 参数
     * @return 返回值
     */
    @Override
    public VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Empty version manifest response"));
            return null;
        }
        try {
            return Json.GSON.fromJson(body.string(), VersionManifest.class);
        } catch (IOException | RuntimeException e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't parse version manifest: " + e.getMessage()));
            return null;
        }
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param configuration 参数
     */
    @Override
    public void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration) {
        Thread thread = new Thread(() -> downloadJavaRuntime(configuration), "java-runtime-download");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 下载 Java Runtime。
     * @param configuration 参数
     */
    private void downloadJavaRuntime(ApplicationConfiguration configuration) {
        try {
            Path javaHome = resolveJavaRuntime();
            if (javaHome == null) {
                SIGNAL_BUS.emit("log.error", LogEvent.of("Can't find java executable in downloaded runtime"));
                return;
            }
            configuration.userSettings().javaHome(javaHome.toString());
            configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
            SIGNAL_BUS.emit("log.info", LogEvent.of("Java runtime installed: " + javaHome));
        } catch (Exception e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't download Java runtime: " + e.getMessage()));
        }
    }

    /**
     * 解析 Java Runtime。
     * @return 返回值
     * @throws Exception 异常
     */
    private Path resolveJavaRuntime() throws Exception {
        Path targetDirectory = Path.of(".simpleminecraftlauncher", "java");
        Files.createDirectories(targetDirectory);
        Path runtimeDirectory = targetDirectory.resolve("runtime");
        Path javaHome = findJavaHome(runtimeDirectory);
        if (javaHome != null) {
            return javaHome;
        }
        return downloadAndExtractRuntime(targetDirectory, runtimeDirectory);
    }

    /**
     * 下载 And Extract Runtime。
     * @param targetDirectory 参数
     * @param runtimeDirectory 参数
     * @return 返回值
     * @throws Exception 异常
     */
    private Path downloadAndExtractRuntime(Path targetDirectory, Path runtimeDirectory) throws Exception {
        boolean windows = OperatingSystem.getOs() == OperatingSystem.WINDOWS;
        Path archive = targetDirectory.resolve("java-runtime." + (windows ? "zip" : "tar.gz"));
        download(javaRuntimeUrl(), archive);
        if (windows) {
            unzip(archive, runtimeDirectory);
        } else {
            untar(archive, runtimeDirectory);
        }
        return findJavaHome(runtimeDirectory);
    }

    /**
     * Java Runtime Url。
     * @return 返回值
     */
    private static String javaRuntimeUrl() {
        String os = switch (OperatingSystem.getOs()) {
            case WINDOWS -> "windows";
            case OSX -> "mac";
            case LINUX -> "linux";
            case null -> "linux";
        };
        String arch = switch (GameDirectory.normalizeArch(System.getProperty("os.arch", "x86_64"))) {
            case "arm64" -> "aarch64";
            case "x86" -> "x86";
            default -> "x64";
        };
        return ("https://api.adoptium.net/v3/binary/latest/%d/ga/%s/%s/jdk/hotspot/normal/eclipse"
                .formatted(JAVA_RUNTIME_FEATURE, os, arch));
    }

    /**
     * 内部方法：download。
     * @param url 参数
     * @param target 参数
     * @throws IOException 异常
     */
    private void download(String url, Path target) throws IOException {
        if (url == null || url.isBlank()) {
            return;
        }
        DownloadEvent event = DownloadEvent.of(url, target);
        PluginManager.get().getSignalBus().emit("download.file", event);
        if (!event.handled()) {
            throw new IOException("Downloader plugin is not loaded");
        }
        event.throwIfFailed();
    }

    /**
     * 内部方法：untar。
     * @param archive 参数
     * @param target 参数
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    private static void untar(Path archive, Path target) throws IOException, InterruptedException {
        Files.createDirectories(target);
        Process process = new ProcessBuilder("tar", "-xzf", archive.toString(), "-C", target.toString())
                .inheritIO()
                .start();
        int exit = process.waitFor();
        if (exit != 0) {
            throw new IOException("tar exited with " + exit);
        }
    }

    /**
     * 内部方法：unzip。
     * @param archive 参数
     * @param target 参数
     * @throws IOException 异常
     */
    private static void unzip(Path archive, Path target) throws IOException {
        Files.createDirectories(target);
        try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(archive))) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                writeZipEntry(zip, entry, target);
            }
        }
    }

    /**
     * 写入 Zip Entry。
     * @param zip 参数
     * @param entry 参数
     * @param target 参数
     * @throws IOException 异常
     */
    private static void writeZipEntry(ZipInputStream zip, ZipEntry entry, Path target) throws IOException {
        Path output = target.resolve(entry.getName()).normalize();
        if (!output.startsWith(target)) {
            return;
        }
        if (entry.isDirectory()) {
            Files.createDirectories(output);
            return;
        }
        Files.createDirectories(output.getParent());
        try (OutputStream stream = Files.newOutputStream(output)) {
            zip.transferTo(stream);
        }
    }

    /**
     * 查找 Java Home。
     * @param root 参数
     * @return 返回值
     */
    private static Path findJavaHome(Path root) {
        if (root == null || !Files.isDirectory(root)) {
            return null;
        }
        try (var stream = Files.walk(root, 4)) {
            return stream.filter(DefaultImplementationVersionManager::isJavaExecutable)
                    .map(path -> path.getParent().getParent())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 判断是否 Java Executable。
     * @param path 参数
     * @return 返回值
     */
    private static boolean isJavaExecutable(Path path) {
        if (!Files.isRegularFile(path) || path.getParent() == null) {
            return false;
        }
        String name = path.getFileName().toString();
        String parent = path.getParent().getFileName().toString();
        return "bin".equals(parent) && ("java".equals(name) || "java.exe".equals(name));
    }
}
