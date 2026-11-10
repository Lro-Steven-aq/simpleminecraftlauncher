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
package org.lro.steven.aq.simpleminecraftlauncher.environment;

import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 运行环境相关的辅助功能（目前是查找本机 Java）。
 */
public final class Environment {

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();
    private static final Pattern RELEASE_VERSION = Pattern.compile("JAVA_VERSION=\"([^\"]+)\"");
    private static final Pattern VERSION_NUMBER = Pattern.compile("(\\d+)(?:\\.(\\d+))?");
    private static final Pattern DIRECTORY_VERSION = Pattern.compile("(?:jdk|java|temurin|graalvm)[-_]?(\\d+)", Pattern.CASE_INSENSITIVE);

    /**
     * 创建 {@code Environment} 实例。
     */
    private Environment() {
    }

    /**
     * 扫描本机常见的 JDK/JRE 安装位置，选出版本最高的一个写入用户配置。
     */
    public static void scanJavaRuntime(ApplicationConfiguration configuration) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Scanning for Java Home.."));
        UserSettings settings = userSettings(configuration);
        if (settings == null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Can't save Java: user settings are missing"));
            return;
        }
        Path javaHome = findBestJavaHome(collectCandidates());
        if (javaHome == null) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("No Java runtime found. Please install a JDK/JRE."));
            return;
        }
        saveJavaHome(configuration, settings, javaHome);
    }

    /**
     * 内部方法：user Settings。
     * @param configuration 参数
     * @return 返回值
     */
    private static UserSettings userSettings(ApplicationConfiguration configuration) {
        return configuration == null ? null : configuration.userSettings();
    }

    /**
     * 内部方法：collect Candidates。
     * @return 返回值
     */
    private static Set<Path> collectCandidates() {
        Set<Path> candidates = new LinkedHashSet<>();
        addJavaHome(candidates, System.getenv("JAVA_HOME"));
        addCommonLocations(candidates);
        addPlatformLocations(candidates);
        return candidates;
    }

    /**
     * 添加 Common Locations。
     * @param candidates 参数
     */
    private static void addCommonLocations(Set<Path> candidates) {
        Path home = Path.of(System.getProperty("user.home", "."));
        addChildren(candidates, home.resolve(".jdks"), false);
        addChildren(candidates, home.resolve("Applications"), false);
        addChildren(candidates, home.resolve(".sdkman").resolve("candidates").resolve("java"), false);
        addChildren(candidates, Path.of("/usr/lib/jvm"), false);
        addChildren(candidates, Path.of("/usr/java"), false);
        addChildren(candidates, Path.of("/opt"), true);
    }

    /**
     * 添加 Platform Locations。
     * @param candidates 参数
     */
    private static void addPlatformLocations(Set<Path> candidates) {
        if (OperatingSystem.getOs() == OperatingSystem.OSX) {
            addMacJavaHomes(candidates, Path.of("/Library/Java/JavaVirtualMachines"));
        }
        if (OperatingSystem.getOs() == OperatingSystem.WINDOWS) {
            addWindowsJavaHomes(candidates);
        }
    }

    /**
     * 添加 Windows Java Homes。
     * @param candidates 参数
     */
    private static void addWindowsJavaHomes(Set<Path> candidates) {
        String programFiles = System.getenv("ProgramFiles");
        if (programFiles == null) {
            return;
        }
        addChildren(candidates, Path.of(programFiles, "Java"), false);
        addChildren(candidates, Path.of(programFiles, "Eclipse Adoptium"), false);
        addChildren(candidates, Path.of(programFiles, "Microsoft"), false);
    }

    /**
     * 查找 Best Java Home。
     * @param candidates 参数
     * @return 返回值
     */
    private static Path findBestJavaHome(Set<Path> candidates) {
        Path best = null;
        int bestVersion = Integer.MIN_VALUE;
        for (Path candidate : candidates) {
            if (!isJavaHome(candidate)) {
                continue;
            }
            int version = detectJavaVersion(candidate);
            if (best == null || version > bestVersion) {
                best = candidate;
                bestVersion = version;
            }
        }
        return best;
    }

    /**
     * 保存 Java Home。
     * @param configuration 参数
     * @param settings 参数
     * @param javaHome 参数
     */
    private static void saveJavaHome(ApplicationConfiguration configuration, UserSettings settings, Path javaHome) {
        settings.javaHome(javaHome.toString());
        configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
        SIGNAL_BUS.emit("log.info", LogEvent.of("Java home: " + javaHome));
    }

    /**
     * 添加 Java Home。
     * @param candidates 参数
     * @param javaHome 参数
     */
    private static void addJavaHome(Set<Path> candidates, String javaHome) {
        if (javaHome == null || javaHome.isBlank()) {
            return;
        }
        try {
            Path path = Path.of(javaHome);
            if (isJavaHome(path)) {
                candidates.add(path.toAbsolutePath().normalize());
            }
        } catch (RuntimeException ignored) {
            // 非法路径直接忽略。
        }
    }

    /**
     * 添加 Children。
     * @param candidates 参数
     * @param directory 参数
     * @param filterNames 参数
     */
    private static void addChildren(Set<Path> candidates, Path directory, boolean filterNames) {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (Stream<Path> stream = Files.list(directory)) {
            addMatchingJavaHomes(candidates, stream, filterNames);
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't scan %s: %s".formatted(directory, e.getMessage())));
        }
    }

    /**
     * 添加 Matching Java Homes。
     * @param candidates 参数
     * @param stream 参数
     * @param filterNames 参数
     */
    private static void addMatchingJavaHomes(Set<Path> candidates, Stream<Path> stream, boolean filterNames) {
        stream.filter(Files::isDirectory)
                .filter(path -> !filterNames || isJavaLike(path))
                .sorted()
                .forEach(path -> addIfJavaHome(candidates, path));
    }

    /**
     * 添加 If Java Home。
     * @param candidates 参数
     * @param path 参数
     */
    private static void addIfJavaHome(Set<Path> candidates, Path path) {
        if (isJavaHome(path)) {
            candidates.add(path.toAbsolutePath().normalize());
        }
    }

    /**
     * 判断是否 Java Like。
     * @param path 参数
     * @return 返回值
     */
    private static boolean isJavaLike(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.contains("jdk") || name.contains("java") || name.contains("temurin") || name.contains("graalvm");
    }

    /**
     * 添加 Mac Java Homes。
     * @param candidates 参数
     * @param directory 参数
     */
    private static void addMacJavaHomes(Set<Path> candidates, Path directory) {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (Stream<Path> stream = Files.list(directory)) {
            stream.filter(Files::isDirectory)
                    .map(path -> path.resolve("Contents").resolve("Home"))
                    .filter(Environment::isJavaHome)
                    .forEach(path -> candidates.add(path.toAbsolutePath().normalize()));
        } catch (IOException e) {
            SIGNAL_BUS.emit("log.warn", LogEvent.of("Can't scan %s: %s".formatted(directory, e.getMessage())));
        }
    }

    /**
     * 判断是否 Java Home。
     * @param home 参数
     * @return 返回值
     */
    private static boolean isJavaHome(Path home) {
        if (home == null) {
            return false;
        }
        boolean windows = OperatingSystem.getOs() == OperatingSystem.WINDOWS;
        return Files.isRegularFile(home.resolve("bin").resolve(windows ? "java.exe" : "java"));
    }

    /**
     * 从 release 文件读取 Java 主版本；读不到时退回目录名。
     */
    private static int detectJavaVersion(Path home) {
        int version = readReleaseVersion(home);
        return version > 0 ? version : directoryVersion(home);
    }

    /**
     * 读取 Release Version。
     * @param home 参数
     * @return 返回值
     */
    private static int readReleaseVersion(Path home) {
        Path release = home.resolve("release");
        if (!Files.isRegularFile(release)) {
            return -1;
        }
        try {
            Matcher matcher = RELEASE_VERSION.matcher(Files.readString(release));
            return matcher.find() ? parseMajor(matcher.group(1)) : -1;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 内部方法：directory Version。
     * @param home 参数
     * @return 返回值
     */
    private static int directoryVersion(Path home) {
        Matcher matcher = DIRECTORY_VERSION.matcher(home.getFileName().toString());
        if (!matcher.find()) {
            return -1;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 解析 Major。
     * @param version 参数
     * @return 返回值
     */
    private static int parseMajor(String version) {
        Matcher matcher = VERSION_NUMBER.matcher(version);
        if (!matcher.find()) {
            return -1;
        }
        try {
            int major = Integer.parseInt(matcher.group(1));
            // 1.8.0_xxx 的写法，主版本是 8。
            if (major == 1 && matcher.group(2) != null) {
                return Integer.parseInt(matcher.group(2));
            }
            return major;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
