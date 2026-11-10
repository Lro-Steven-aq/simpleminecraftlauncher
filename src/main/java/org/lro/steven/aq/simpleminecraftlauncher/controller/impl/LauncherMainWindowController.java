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
package org.lro.steven.aq.simpleminecraftlauncher.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.environment.Environment;
import org.lro.steven.aq.simpleminecraftlauncher.json.Json;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.launch.GameDirectory;
import org.lro.steven.aq.simpleminecraftlauncher.launch.LaunchConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.Version;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LaunchEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;
import org.lro.steven.aq.simpleminecraftlauncher.utils.UIManager;
import org.lro.steven.aq.simpleminecraftlauncher.version.VersionManager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** 启动器主窗口的控制器。 */
public class LauncherMainWindowController extends Controller {

    @FXML
    private ComboBox<String> versionSelector;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView frostImage;

    @FXML
    private Button launchButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Pane root;

    @FXML
    private AnchorPane rightPane;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button exitButton;

    @FXML
    private MenuItem enableFastLaunchButton;

    @FXML
    private MenuItem disableFastLaunchButton;

    @FXML
    private Menu downloadsButton;

    @FXML
    private MenuItem downloadNewVersionsButton;

    @FXML
    private Menu helpButton;

    @FXML
    private MenuItem aboutButton;

    // 如果可以，尽量不要在Controller里面塞非@FXML字段。
    // 已有的不用改。

    private final Metadata metadata;

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();

    private final VersionManager versionManager;

    /** 系统不支持磨砂时，右侧面板使用的样式类。 */
    private static final String NO_FROST_CLASS = "no-frost";

    /** 内置磨砂层的模糊半径。 */
    private static final double FROST_BLUR_RADIUS = 40.0;

    /** 系统不支持磨砂时，背景图几乎不透明。 */
    private static final double FALLBACK_IMAGE_OPACITY = 0.95;

    /**
     * 创建主窗口控制器。
     * @param metadata 参数
     */
    public LauncherMainWindowController(Metadata metadata) {
        this.metadata = metadata;
        this.versionManager = VersionManager.create(metadata.configurationMapper());
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param location 参数
     * @param resources 参数
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStyle();
        initializeVersionSelector();
        bindWindowControls();
        launchButton.setOnMouseClicked(this::launch);
    }

    /**
     * 初始化 Style。
     */
    private void initializeStyle() {
        var image = metadata.preciousBackgroundImage();
        backgroundImage.setImage(image);
        // 左侧保留清晰背景图；右侧用同一张图的模糊副本做磨砂底层。
        backgroundImage.setClip(new Rectangle(0, 0, rightPane.getLayoutX(), root.getPrefHeight()));

        frostImage.setImage(image);
        Rectangle frostClip = new Rectangle();
        frostClip.xProperty().bind(rightPane.layoutXProperty());
        frostClip.widthProperty().bind(rightPane.widthProperty());
        frostClip.heightProperty().bind(root.heightProperty());
        frostImage.setClip(frostClip);
        frostImage.setEffect(new GaussianBlur(FROST_BLUR_RADIUS));
        frostImage.setMouseTransparent(true);
        applyOpacity();
    }

    /**
     * 透明度只作用于背景图和磨砂层，文字与控件保持不透明。
     */
    private void applyOpacity() {
        double alpha = configuredAlpha();
        backgroundImage.setOpacity(alpha);
        frostImage.setOpacity(alpha);
    }

    /**
     * 根据系统磨砂是否生效切换方案：
     * 系统支持时隐藏内置模糊层、由系统模糊桌面；不支持时显示内置模糊背景副本。
     * @param frosted 系统磨砂是否生效
     */
    public void applyFrostState(boolean frosted) {
        frostImage.setVisible(!frosted);
        if (frosted) {
            rightPane.getStyleClass().remove(NO_FROST_CLASS);
            applyOpacity();
            return;
        }
        if (!rightPane.getStyleClass().contains(NO_FROST_CLASS)) {
            rightPane.getStyleClass().add(NO_FROST_CLASS);
        }
        double fallback = Math.max(FALLBACK_IMAGE_OPACITY, configuredAlpha());
        backgroundImage.setOpacity(fallback);
        frostImage.setOpacity(fallback);
    }

    /**
     * 用户配置的背景图透明度；没有配置时按不透明处理。
     * @return 0 到 1 之间的透明度
     */
    private double configuredAlpha() {
        UserSettings settings = metadata.configurationMapper().userSettings();
        if (settings == null || settings.alpha() == null) {
            return 1.0;
        }
        return Math.max(0.0, Math.min(1.0, settings.alpha()));
    }

    /**
     * 初始化 Version Selector。
     */
    private void initializeVersionSelector() {
        List<String> versions = versionManager.listAvailableVersions();
        versionSelector.getItems().setAll(versions);
        UserSettings settings = metadata.configurationMapper().userSettings();
        if (settings == null || settings.lastVersionJsonFile() == null) {
            return;
        }
        Path parent = Path.of(settings.lastVersionJsonFile()).getParent();
        if (parent != null && versions.contains(parent.getFileName().toString())) {
            versionSelector.setValue(parent.getFileName().toString());
        }
    }

    /**
     * 绑定 Window Controls。
     */
    private void bindWindowControls() {
        bindDrag();
        exitButton.setOnMouseClicked(event -> {
            metadata.save();
            Platform.exit();
            System.exit(0);
        });
        minimizeButton.setOnMouseClicked(event -> minimize());
        enableFastLaunchButton.setOnAction(event -> {
            SIGNAL_BUS.emit("log.info", LogEvent.of("switch to fastLaunch mode"));
            metadata.configurationMapper().userSettings().fastLaunch(true);
        });
        disableFastLaunchButton.setOnAction(event -> {
            SIGNAL_BUS.emit("log.info", LogEvent.of("switch off fastLaunch mode"));
            metadata.configurationMapper().userSettings().fastLaunch(false);
        });
        downloadNewVersionsButton.setOnAction(event -> downloadNewVersions());
        aboutButton.setOnAction(event -> showAbout());

    }

    /**
     * 绑定 Drag。
     */
    private void bindDrag() {
        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];
        menuBar.setOnMousePressed(event -> onMenuPressed(event, offsetX, offsetY));
        menuBar.setOnMouseDragged(event -> onMenuDragged(event, offsetX, offsetY));
    }

    /**
     * 处理 Menu Pressed。
     * @param event 参数
     * @param offsetX 参数
     * @param offsetY 参数
     */
    private void onMenuPressed(MouseEvent event, double[] offsetX, double[] offsetY) {
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        Stage stage = (Stage) menuBar.getScene().getWindow();
        offsetX[0] = event.getScreenX() - stage.getX();
        offsetY[0] = event.getScreenY() - stage.getY();
        menuBar.requestFocus();
        event.consume();
    }

    /**
     * 处理 Menu Dragged。
     * @param event 参数
     * @param offsetX 参数
     * @param offsetY 参数
     */
    private void onMenuDragged(MouseEvent event, double[] offsetX, double[] offsetY) {
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setX(event.getScreenX() - offsetX[0]);
        stage.setY(event.getScreenY() - offsetY[0]);
    }

    /**
     * 内部方法：minimize。
     */
    private void minimize() {
        if (root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
            stage.setIconified(true);
        }
    }

    /**
     * 内部方法：launch。
     * @param event 参数
     */
    private void launch(MouseEvent event) {
        launchButton.setDisable(true);
        try {
            startLaunch();
        } catch (Exception e) {
            fail("Launch failed: " + e);
        }
    }

    /**
     * 启动 Launch。
     * @throws IOException 异常
     */
    private void startLaunch() throws IOException {
        UserSettings settings = requireSettings();
        if (settings == null) {
            return;
        }
        String javaExecutable = resolveLaunchJava();
        if (javaExecutable == null) {
            launchButton.setDisable(false);
            return;
        }
        String jsonPath = selectedVersionJsonPath(settings);
        if (jsonPath == null) {
            installVersionAndLaunch();
            return;
        }
        String missingVersion = missingClientJarVersion(jsonPath);
        if (missingVersion != null) {
            startInstall(missingVersion, true);
            return;
        }
        executeLaunch(jsonPath, javaExecutable);
    }

    /**
     * 内部方法：missing Client Jar Version。
     * @param jsonPath 参数
     * @return 返回值
     */
    private String missingClientJarVersion(String jsonPath) {
        try {
            GameMetadataManifest manifest = Json.GSON.fromJson(
                    Files.readString(Path.of(jsonPath)), GameMetadataManifest.class);
            if (manifest == null || manifest.id() == null) {
                return null;
            }
            Path gameDirectory = GameDirectory.resolve(metadata.configurationMapper());
            Path versionDirectory = GameDirectory.versionDirectory(gameDirectory, manifest.id());
            String jarName = manifest.jar() != null && !manifest.jar().isBlank()
                    ? manifest.jar()
                    : manifest.id();
            return Files.isRegularFile(versionDirectory.resolve(jarName + ".jar"))
                    ? null
                    : manifest.id();
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    /**
     * 内部方法：require Settings。
     * @return 返回值
     */
    private UserSettings requireSettings() {
        UserSettings settings = metadata.configurationMapper().userSettings();
        if (settings == null) {
            fail("User settings are missing");
        }
        return settings;
    }

    /**
     * 解析 Launch Java。
     * @return 返回值
     */
    private String resolveLaunchJava() {
        ApplicationConfiguration configuration = metadata.configurationMapper();
        UserSettings settings = configuration.userSettings();
        if (settings == null) {
            fail("User settings are missing");
            return null;
        }
        String javaExecutable = resolveJavaExecutable(configuration, settings);
        if (javaExecutable == null || !Files.isRegularFile(Path.of(javaExecutable))) {
            showJavaPopup(configuration);
            return null;
        }
        return javaExecutable;
    }

    /**
     * 执行 Launch。
     * @param jsonPath 参数
     * @param javaExecutable 参数
     * @throws IOException 异常
     */
    private void executeLaunch(String jsonPath, String javaExecutable) throws IOException {
        String content = Files.readString(Path.of(jsonPath));
        GameMetadataManifest manifest = Json.GSON.fromJson(content, GameMetadataManifest.class);
        if (!isValidManifest(manifest)) {
            fail("Invalid version JSON: " + jsonPath);
            return;
        }
        ApplicationConfiguration configuration = metadata.configurationMapper();
        Path gameDirectory = GameDirectory.resolve(configuration);
        Configuration launchConfiguration = LaunchConfiguration.create(
                configuration, manifest, gameDirectory, metadata.features());
        LaunchEvent event = LaunchEvent.of(
                manifest.id(), content, launchConfiguration, metadata.features(), javaExecutable);
        startGame(event);
    }

    /**
     * 判断是否 Valid Manifest。
     * @param manifest 参数
     * @return 返回值
     */
    private static boolean isValidManifest(GameMetadataManifest manifest) {
        return manifest != null && manifest.mainClass() != null && !manifest.mainClass().isBlank();
    }

    /**
     * 启动 Game。
     * @param event 参数
     */
    private void startGame(LaunchEvent event) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Starting Minecraft " + event.version()));
        Thread thread = new Thread(() -> runGame(event), "minecraft-launch");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 执行 Game。
     * @param event 参数
     */
    private void runGame(LaunchEvent event) {
        SIGNAL_BUS.emit("launch.game", event);
        Platform.runLater(() -> launchButton.setDisable(false));
    }

    /**
     * 内部方法：fail。
     * @param message 参数
     */
    private void fail(String message) {
        SIGNAL_BUS.emit("log.error", LogEvent.of(message));
        Platform.runLater(() -> launchButton.setDisable(false));
    }

    /**
     * 安装 Version And Launch。
     */
    private void installVersionAndLaunch() {
        versionManager.enqueue(new ManifestCallback(true));
    }

    /**
     * 下载 New Versions。
     */
    private void downloadNewVersions() {
        launchButton.setDisable(true);
        versionManager.enqueue(new ManifestCallback(false));
    }

    /**
     * 显示 About。
     */
    private void showAbout() {
        UIManager.showAbout(root.getScene() == null ? null : root.getScene().getWindow());
    }

    /** 版本清单请求的回调，负责选择并安装版本。 */
    private final class ManifestCallback implements Callback {
        private final boolean launchAfterInstall;

        /**
         * 创建 {@code ManifestCallback} 实例。
         * @param launchAfterInstall 参数
         */
        private ManifestCallback(boolean launchAfterInstall) {
            this.launchAfterInstall = launchAfterInstall;
        }

        /**
         * 实现/覆写接口或父类方法。
         * @param call 参数
         * @param e 参数
         */
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            fail("Network error. Try again: " + e.getMessage());
        }

        /**
         * 实现/覆写接口或父类方法。
         * @param call 参数
         * @param response 参数
         */
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {
            try (response) {
                VersionManifest manifest = versionManager.parseVersionManifest(call, response);
                if (manifest == null) {
                    fail("Can't read version manifest");
                    return;
                }
                Platform.runLater(() -> selectAndInstall(manifest, launchAfterInstall));
            }
        }
    /**
     * 内部方法：select And Install。
     * @param manifest 参数
     * @param launchAfterInstall 参数
     * @return 返回值
     */
    }

    private void selectAndInstall(VersionManifest manifest, boolean launchAfterInstall) {
        String version = userSelectVersion(manifest);
        if (version == null) {
            launchButton.setDisable(false);
            return;
        }
        /**
         * 启动 Install。
         * @param version 参数
         * @param launchAfterInstall 参数
         */
        startInstall(version, launchAfterInstall);
    /**
     * 启动 Install。
     * @param version 参数
     * @param launchAfterInstall 参数
     * @return 返回值
     */
    }

    private void startInstall(String version, boolean launchAfterInstall) {
        Thread installer = new Thread(() -> installThenRelaunch(version, launchAfterInstall), "version-install");
        /**
         * 设置 Daemon。
         * @param true 参数
         * @return 返回值
         */
        installer.setDaemon(true);
        /**
         * 内部方法：start。
         * @return 返回值
         */
        installer.start();
    /**
     * 安装 Then Relaunch。
     * @param version 参数
     * @param launchAfterInstall 参数
     * @return 返回值
     */
    }

    private void installThenRelaunch(String version, boolean launchAfterInstall) {
        /**
         * 内部方法：install。
         * @param version 参数
         * @return 返回值
         */
        versionManager.install(version);
        /**
         * 执行 Later。
         * @param afterInstall(version 参数
         * @param launchAfterInstall) 参数
         * @return 返回值
         */
        Platform.runLater(() -> afterInstall(version, launchAfterInstall));
    /**
     * 内部方法：after Install。
     * @param version 参数
     * @param launchAfterInstall 参数
     * @return 返回值
     */
    }

    private void afterInstall(String version, boolean launchAfterInstall) {
        List<String> versions = versionManager.listAvailableVersions();
        /**
         * 获取 Items。
         * @return 返回值
         */
        versionSelector.getItems().setAll(versions);
        /**
         * 设置 Value。
         * @param version 参数
         * @return 返回值
         */
        versionSelector.setValue(version);
        /**
         * 设置 Disable。
         * @param false 参数
         * @return 返回值
         */
        launchButton.setDisable(false);
        if (launchAfterInstall) {
            launch(null);
        }
    /**
     * 解析 Java Executable。
     * @param configuration 参数
     * @param settings 参数
     * @return 返回值
     */
    }

    private String resolveJavaExecutable(ApplicationConfiguration configuration, UserSettings settings) {
        if (settings.javaHome() != null) {
            return javaExecutableIn(settings.javaHome());
        }
        if (settings.executableJavaCommand() != null) {
            return settings.executableJavaCommand();
        }
        /**
         * Java Executable From Environment。
         * @param configuration 参数
         * @param settings 参数
         * @return 返回值
         */
        return javaExecutableFromEnvironment(configuration, settings);
    /**
     * Java Executable From Environment。
     * @param configuration 参数
     * @param settings 参数
     * @return 返回值
     */
    }

    private String javaExecutableFromEnvironment(ApplicationConfiguration configuration, UserSettings settings) {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            return null;
        }
        /**
         * Java Home。
         * @param javaHome 参数
         * @return 返回值
         */
        settings.javaHome(javaHome);
        String executable = javaExecutableIn(javaHome);
        if (executable != null && Files.isRegularFile(Path.of(executable))) {
            configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
        }
        return executable;
    /**
     * 显示 Java Popup。
     * @param configuration 参数
     * @return 返回值
     */
    }

    private void showJavaPopup(ApplicationConfiguration configuration) {
        /**
         * 显示 Java Prompt。
         * @param this::noChoice 参数
         * @return 返回值
         */
        UIManager.showJavaPrompt(
                () -> {
                    Environment.scanJavaRuntime(configuration);
                    launch(null);
                },
                () -> versionManager.downloadJavaRuntimeFromInternet(configuration),
                this::noChoice);
    /**
     * Java Executable In。
     * @param javaHome 参数
     * @return 返回值
     */
    }

    private static String javaExecutableIn(String javaHome) {
        String executable = OperatingSystem.getOs() == OperatingSystem.WINDOWS ? "java.exe" : "java";
        /**
         * 内部方法：of。
         * @param javaHome 参数
         * @param "bin" 参数
         * @param executable 参数
         * @return 返回值
         */
        return Path.of(javaHome, "bin", executable).toString();
    /**
     * 内部方法：selected Version Json Path。
     * @param settings 参数
     * @return 返回值
     */
    }

    private String selectedVersionJsonPath(UserSettings settings) {
        String selected = versionSelector.getValue();
        if (selected != null) {
            Path candidate = GameDirectory.versionDirectory(
                            GameDirectory.resolve(metadata.configurationMapper()), selected)
                    .resolve(selected + ".json");
            if (Files.isRegularFile(candidate)) {
                return candidate.toString();
            }
        }
        String configured = settings.lastVersionJsonFile();
        if (configured != null && Files.isRegularFile(Path.of(configured))) {
            return configured;
        }
        return null;
    /**
     * 内部方法：user Select Version。
     * @param manifest 参数
     * @return 返回值
     */
    }

    private String userSelectVersion(VersionManifest manifest) {
        List<String> versions = versionIds(manifest);
        if (versions.isEmpty()) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("No versions available"));
            return null;
        }
        /**
         * 内部方法：choose Version。
         * @param root.getScene().getWindow() 参数
         * @param versions 参数
         * @param versions) 参数
         * @param version" 参数
         * @param install" 参数
         * @param "Version:" 参数
         * @return 返回值
         */
        return UIManager.chooseVersion(
                        root.getScene() == null ? null : root.getScene().getWindow(),
                        versions,
                        initialVersion(manifest, versions),
                        "Minecraft version",
                        "Select the version to install",
                        "Version:")
                .orElse(null);
    /**
     * 内部方法：version Ids。
     * @param manifest 参数
     * @return 返回值
     */
    }

    private static List<String> versionIds(VersionManifest manifest) {
        if (manifest == null || manifest.versions() == null) {
            return List.of();
        }
        List<String> versions = new ArrayList<>();
        for (Version version : manifest.versions()) {
            if (version != null && version.id() != null) {
                versions.add(version.id());
            }
        }
        return versions;
    /**
     * 内部方法：initial Version。
     * @param manifest 参数
     * @param versions 参数
     * @return 返回值
     */
    }

    private static String initialVersion(VersionManifest manifest, List<String> versions) {
        String initial = manifest.latest() != null && manifest.latest().release() != null
                /**
                 * 内部方法：latest。
                 * @return 返回值
                 */
                ? manifest.latest().release()
                : versions.get(0);
        /**
         * 内部方法：contains。
         * @param initial 参数
         * @return 返回值
         */
        return versions.contains(initial) ? initial : versions.get(0);
    /**
     * 内部方法：no Choice。
     * @return 返回值
     */
    }

    private void noChoice() {
        /**
         * 内部方法：emit。
         * @param "log.error" 参数
         * @param choice") 参数
         * @return 返回值
         */
        SIGNAL_BUS.emit("log.error", LogEvent.of("No choice"));
    }
}
