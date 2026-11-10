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
package org.lro.steven.aq.simpleminecraftlauncher;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.lro.steven.aq.simpleminecraftlauncher.json.Json;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.controller.impl.LauncherMainWindowController;
import org.lro.steven.aq.simpleminecraftlauncher.launch.GameDirectory;
import org.lro.steven.aq.simpleminecraftlauncher.launch.LaunchConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LaunchEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.window.DesktopFrost;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/** 启动器主类：加载配置、构建界面并在 JavaFX 中显示主窗口。 */
public class Application extends javafx.application.Application {

    private LauncherMainWindowController launcherController;
    private static ApplicationConfiguration configurations;

    /**
     * 实现/覆写接口或父类方法。
     * @param primaryStage 参数
     * @throws Exception 异常
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Metadata metadata = Metadata.from(Features.create(new HashMap<>()), configurations);
        Parent root = loadRoot(metadata);
        Font.loadFont(getClass().getResourceAsStream("LXGWWenKaiMono-Regular.ttf"), 48);
        showStage(primaryStage, root);
    }

    /**
     * 加载 Root。
     * @param metadata 参数
     * @return 返回值
     * @throws IOException 异常
     */
    private Parent loadRoot(Metadata metadata) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
        loader.setControllerFactory(controllerClass -> createController(controllerClass, metadata));
        Parent root = loader.load();
        if (loader.getController() instanceof LauncherMainWindowController controller) {
            launcherController = controller;
        }
        return root;
    }

    /**
     * 创建 Controller。
     * @param controllerClass 参数
     * @param metadata 参数
     * @return 返回值
     */
    private Object createController(Class<?> controllerClass, Metadata metadata) {
        if (controllerClass.equals(LauncherMainWindowController.class)) {
            return new LauncherMainWindowController(metadata);
        }
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 显示 Stage。
     * @param stage 参数
     * @param root 参数
     */
    private void showStage(Stage stage, Parent root) {
        stage.setTitle("Simple Launcher");
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        applyDesktopFrost(stage.getTitle(), false);
        // 调试启动时原生窗口就绪可能稍慢，延迟再补一次；成功则把回退样式升级为磨砂样式。
        PauseTransition retry = new PauseTransition(Duration.millis(600));
        retry.setOnFinished(event -> applyDesktopFrost(stage.getTitle(), true));
        retry.play();
    }

    /**
     * 开启桌面磨砂，并把结果交给控制器选择对应的透明方案。
     * @param windowTitle 窗口标题
     * @param retry 是否为延迟重试；重试只在成功时才重新应用样式
     */
    private void applyDesktopFrost(String windowTitle, boolean retry) {
        boolean frosted = DesktopFrost.apply(windowTitle);
        if (launcherController == null || (retry && !frosted)) {
            return;
        }
        launcherController.applyFrostState(frosted);
    }

    /**
     * JavaFX 入口：启动插件系统并启动应用。
     * @param args 参数
     * @throws IOException 异常
     */
    public static void _main(String[] args) throws IOException {
        configurations = loadConfiguration().getConfigurations();
        Runtime.getRuntime().addShutdownHook(new Thread(Application::saveConfiguration, "save-metadata"));
        // start signalBus to load the plugins.
        PluginManager manager = PluginManager.get();

        manager.startAll();
        manager.getSignalBus().emit("log.info", LogEvent.of("initialize"));
        // ///////////////////////////////////
        UserSettings settings = configurations.userSettings();
        if (settings == null || !settings.fastLaunch()) {
            javafx.application.Application.launch(Application.class, args);
            return;
        }
        fastLaunch(args);
    }

    /**
     * 快速启动：不创建 JavaFX 窗口，直接用上次的版本 JSON 启动游戏。
     * 没有可用的已安装版本时回退到正常 UI。
     * @param args 启动参数
     * @throws IOException 读取配置/版本文件失败
     */
    private static void fastLaunch(String[] args) {
        LaunchEvent launch;
        try {
            launch = createFastLaunchEvent(configurations);
        } catch (IOException | RuntimeException e) {
            PluginManager.get().getSignalBus().emit("log.error",
                    LogEvent.of("Fast launch failed: " + e.getMessage() + "; opening the launcher."));
            javafx.application.Application.launch(Application.class, args);
            return;
        }
        if (launch == null) {
            PluginManager.get().getSignalBus().emit("log.warn",
                    LogEvent.of("Fast launch is enabled, but no installed version is available; opening the launcher."));
            javafx.application.Application.launch(Application.class, args);
            return;
        }
        PluginManager.get().getSignalBus().emit("log.info", LogEvent.of("Fast launch"));
        PluginManager.get().getSignalBus().emit("launch.fast", launch);
        System.exit(0);
    }

    /**
     * 构建快速启动事件；版本 JSON、客户端 jar 或主类缺失时返回 null。
     * @param configuration 启动器配置
     * @return 启动事件，或 null
     * @throws IOException 读取版本 JSON 失败
     */
    private static LaunchEvent createFastLaunchEvent(ApplicationConfiguration configuration) throws IOException {
        UserSettings settings = configuration.userSettings();
        if (settings == null || settings.lastVersionJsonFile() == null
                || settings.lastVersionJsonFile().isBlank()) {
            return null;
        }
        Path jsonFile = Path.of(settings.lastVersionJsonFile());
        if (!Files.isRegularFile(jsonFile)) {
            return null;
        }
        String content = Files.readString(jsonFile);
        GameMetadataManifest manifest = Json.GSON.fromJson(content, GameMetadataManifest.class);
        if (manifest == null || manifest.id() == null || manifest.mainClass() == null) {
            return null;
        }
        Path gameDirectory = GameDirectory.resolve(configuration);
        Path versionDirectory = GameDirectory.versionDirectory(gameDirectory, manifest.id());
        String jarName = manifest.jar() != null && !manifest.jar().isBlank()
                ? manifest.jar()
                : manifest.id();
        if (!Files.isRegularFile(versionDirectory.resolve(jarName + ".jar"))) {
            return null;
        }
        Features features = Features.create(new HashMap<>());
        Configuration launchConfiguration = LaunchConfiguration.create(
                configuration, manifest, gameDirectory, features);
        return LaunchEvent.of(manifest.id(), content, launchConfiguration, features,
                resolveJavaExecutable(settings));
    }

    /**
     * 优先使用配置里的 Java，其次使用启动器自身的 Java。
     * @param settings 用户设置
     * @return java 可执行文件路径
     */
    private static String resolveJavaExecutable(UserSettings settings) {
        String executable = settings.executableJavaCommand();
        if (executable != null && Files.isRegularFile(Path.of(executable))) {
            return executable;
        }
        String javaHome = settings.javaHome();
        if (javaHome != null) {
            Path candidate = Path.of(javaHome, "bin", javaExecutableName());
            if (Files.isRegularFile(candidate)) {
                return candidate.toString();
            }
        }
        Path current = Path.of(System.getProperty("java.home", ""), "bin", javaExecutableName());
        return Files.isRegularFile(current) ? current.toString() : "java";
    }

    /**
     * Java Executable Name。
     * @return 返回值
     */
    private static String javaExecutableName() {
        return OperatingSystem.getOs() == OperatingSystem.WINDOWS ? "java.exe" : "java";
    }

    /**
     * 加载默认配置文件。
     * @return 返回值
     */
    public static ApplicationConfigurationLoader loadConfiguration() {
        ApplicationConfigurationLoader configuration = new ApplicationConfigurationLoader();
        configuration.load(new File(ApplicationConfigurationLoader.DEFAULT_PATH));
        return configuration;
    }

    /**
     * 保存当前配置；由 JVM 退出钩子调用，覆盖正常退出和快速启动退出。
     */
    private static void saveConfiguration() {
        if (configurations == null) {
            return;
        }
        try {
            configurations.save(ApplicationConfigurationLoader.DEFAULT_PATH);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
