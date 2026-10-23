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
package org.lro.steven.aq.simpleminecraftlauncher.version.impl;

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
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;
import org.lro.steven.aq.simpleminecraftlauncher.version.VersionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * VersionManager 的默认实现：从 Mojang 版本清单下载并安装版本。
 */
public class DefaultImplementationVersionManager implements VersionManager {

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final int JAVA_RUNTIME_FEATURE = 25;

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

    @Override
    public VersionManifest getVersionManifest() {
        if (cachedManifest != null) {
            return cachedManifest;
        }
        try (Response response = execute(versionManifestUrl.toString())) {
            cachedManifest = readManifest(response);
            return cachedManifest;
        } catch (IOException | RuntimeException e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't download version manifest: " + e.getMessage()));
            return null;
        }
    }

    private VersionManifest readManifest(Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't download version manifest: HTTP " + response.code()));
            return null;
        }
        return Json.GSON.fromJson(response.body().string(), VersionManifest.class);
    }

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

    private static void logInstalled(String version) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Version installed: " + version));
        SIGNAL_BUS.emit("log.warn", LogEvent.of(
                "Asset objects are not downloaded; run the official launcher once if textures/sounds are missing."));
    }

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

    private void downloadVersionJson(Version selected, Path versionDirectory, Path jsonFile) throws IOException {
        Files.createDirectories(versionDirectory);
        SIGNAL_BUS.emit("log.info", LogEvent.of("Downloading version JSON: " + selected.id()));
        download(selected.url().toString(), jsonFile);
    }

    private void installVersionFiles(Path gameDirectory, Path versionDirectory, String version, Path jsonFile) throws IOException {
        GameMetadataManifest metadata = Json.GSON.fromJson(Files.readString(jsonFile), GameMetadataManifest.class);
        downloadClientJar(versionDirectory, version, metadata);
        downloadLibraries(gameDirectory, metadata);
        downloadAssetIndex(gameDirectory, metadata);
    }

    private void saveInstalledVersion(Path jsonFile) {
        configuration.userSettings().lastVersionJsonFile(jsonFile.toString());
        configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
    }

    private void downloadClientJar(Path versionDirectory, String version, GameMetadataManifest metadata) throws IOException {
        if (metadata == null || metadata.downloads() == null || metadata.downloads().client() == null
                || metadata.downloads().client().url() == null) {
            return;
        }
        String jarName = metadata.jar() != null && !metadata.jar().isBlank() ? metadata.jar() : version;
        download(metadata.downloads().client().url().toString(), versionDirectory.resolve(jarName + ".jar"));
    }

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

    private boolean downloadLibraryJar(String url, Path target, String path) {
        try {
            download(url, target);
            return true;
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't download library %s: %s".formatted(path, e.getMessage())));
            return false;
        }
    }

    private String artifactUrl(Library library) {
        if (library.downloads() == null || library.downloads().artifact() == null
                || library.downloads().artifact().url() == null) {
            return null;
        }
        return library.downloads().artifact().url().toString();
    }

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

    @Override
    public void enqueue(Callback callback) {
        Request request = new Request.Builder().url(versionManifestUrl.toString()).build();
        HTTP_CLIENT.newCall(request).enqueue(callback);
    }

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

    @Override
    public void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration) {
        Thread thread = new Thread(() -> downloadJavaRuntime(configuration), "java-runtime-download");
        thread.setDaemon(true);
        thread.start();
    }

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

    private Path downloadAndExtractRuntime(Path targetDirectory, Path runtimeDirectory) throws Exception {
        boolean windows = OperatingSystem.getOs() == OperatingSystem.WINDOWS;
        Path archive = targetDirectory.resolve("java-runtime." + (windows ? "zip" : "tar.gz"));
        downloadResumable(javaRuntimeUrl(), archive);
        if (windows) {
            unzip(archive, runtimeDirectory);
        } else {
            untar(archive, runtimeDirectory);
        }
        return findJavaHome(runtimeDirectory);
    }

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

    private void download(String url, Path target) throws IOException {
        if (url == null || url.isBlank()) {
            return;
        }
        if (Files.isRegularFile(target) && Files.size(target) > 0) {
            return;
        }
        try (Response response = execute(url)) {
            writeBody(response, target);
        }
    }

    private static void writeBody(Response response, Path target) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("HTTP " + response.code());
        }
        Files.createDirectories(target.getParent());
        try (InputStream input = response.body().byteStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Response execute(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return HTTP_CLIENT.newCall(request).execute();
    }

    /**
     * 支持断点续传的下载：先写入 .part，完成后再移动到目标文件。
     * <br/>
     * 如果之前的 .part 已存在，会带 Range 头从已有字节处继续。
     */
    private void downloadResumable(String url, Path target) throws IOException {
        if (Files.isRegularFile(target) && Files.size(target) > 0) {
            return;
        }
        downloadResumable(url, target, false);
    }

    private void downloadResumable(String url, Path target, boolean restarted) throws IOException {
        Path part = partFile(target);
        long existing = sizeOf(part);
        try (Response response = requestWithRange(url, existing)) {
            if (response.code() == 416) {
                handleUnsatisfiableRange(url, target, part, response, restarted);
                return;
            }
            downloadIntoPart(response, part, target, existing, url);
        }
    }

    private void handleUnsatisfiableRange(String url, Path target, Path part, Response response, boolean restarted)
            throws IOException {
        if (moveIfComplete(part, target, response)) {
            return;
        }
        Files.deleteIfExists(part);
        if (restarted) {
            throw new IOException("Server rejected range request: " + url);
        }
        downloadResumable(url, target, true);
    }

    private static void downloadIntoPart(Response response, Path part, Path target, long existing, String url)
            throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("HTTP " + response.code() + " for " + url);
        }
        boolean append = response.code() == 206;
        if (!append) {
            Files.deleteIfExists(part);
            existing = 0;
        }
        appendBody(response, part, append);
        verifyComplete(part, response, existing);
        Files.move(part, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private static Response requestWithRange(String url, long existing) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (existing > 0) {
            builder.header("Range", "bytes=" + existing + "-");
        }
        return HTTP_CLIENT.newCall(builder.build()).execute();
    }

    private static void appendBody(Response response, Path part, boolean append) throws IOException {
        Files.createDirectories(part.getParent());
        OpenOption[] options = append
                ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        try (InputStream input = response.body().byteStream();
             OutputStream output = Files.newOutputStream(part, options)) {
            input.transferTo(output);
        }
    }

    private static void verifyComplete(Path part, Response response, long existing) throws IOException {
        long total = totalSize(response, existing);
        if (total > 0 && Files.size(part) < total) {
            throw new IOException("Incomplete download: " + Files.size(part) + "/" + total);
        }
    }

    private static long totalSize(Response response, long existing) {
        long total = contentRangeTotal(response.header("Content-Range"));
        if (total > 0) {
            return total;
        }
        String contentLength = response.header("Content-Length");
        return contentLength == null ? 0 : existing + parseLong(contentLength);
    }

    private static long contentRangeTotal(String header) {
        if (header == null) {
            return 0;
        }
        int slash = header.lastIndexOf('/');
        return slash < 0 ? 0 : parseLong(header.substring(slash + 1));
    }

    private static long parseLong(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static boolean moveIfComplete(Path part, Path target, Response response) throws IOException {
        long total = contentRangeTotal(response.header("Content-Range"));
        if (total > 0 && Files.isRegularFile(part) && Files.size(part) == total) {
            Files.move(part, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        return false;
    }

    private static Path partFile(Path target) {
        return target.resolveSibling(target.getFileName() + ".part");
    }

    private static long sizeOf(Path path) throws IOException {
        return Files.isRegularFile(path) ? Files.size(path) : 0;
    }

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

    private static void unzip(Path archive, Path target) throws IOException {
        Files.createDirectories(target);
        try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(archive))) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                writeZipEntry(zip, entry, target);
            }
        }
    }

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

    private static boolean isJavaExecutable(Path path) {
        if (!Files.isRegularFile(path) || path.getParent() == null) {
            return false;
        }
        String name = path.getFileName().toString();
        String parent = path.getParent().getFileName().toString();
        return "bin".equals(parent) && ("java".equals(name) || "java.exe".equals(name));
    }
}
