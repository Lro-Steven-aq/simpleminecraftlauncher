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
package org.lro.steven.aq.simpleminecraftlauncher.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.environment.Environment;
import org.lro.steven.aq.simpleminecraftlauncher.json.Json;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Parser;
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
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;
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
    private Button launchButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Pane root;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button exitButton;

    private final Metadata metadata;

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();

    private final VersionManager versionManager;

    /**
     * 创建主窗口控制器。
     * @param metadata 参数
     */
    public LauncherMainWindowController(Metadata metadata) {
        this.metadata = metadata;
        this.versionManager = VersionManager.create(metadata.configurationMapper());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStyle();
        initializeVersionSelector();
        bindWindowControls();
        launchButton.setOnMouseClicked(this::launch);
    }

    private void initializeStyle() {
        backgroundImage.setImage(metadata.preciousBackgroundImage());
        applyOpacity();
        exitButton.setOpacity(0.7);
        minimizeButton.setOpacity(0.7);
        launchButton.setOpacity(0.7);
    }

    private void applyOpacity() {
        UserSettings settings = metadata.configurationMapper().userSettings();
        if (settings != null && settings.alpha() != null) {
            root.setOpacity(settings.alpha());
        }
    }

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

    private void bindWindowControls() {
        bindDrag();
        exitButton.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        minimizeButton.setOnMouseClicked(event -> minimize());
    }

    private void bindDrag() {
        final double[] offsetX = new double[1];
        final double[] offsetY = new double[1];
        menuBar.setOnMousePressed(event -> onMenuPressed(event, offsetX, offsetY));
        menuBar.setOnMouseDragged(event -> onMenuDragged(event, offsetX, offsetY));
    }

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

    private void onMenuDragged(MouseEvent event, double[] offsetX, double[] offsetY) {
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setX(event.getScreenX() - offsetX[0]);
        stage.setY(event.getScreenY() - offsetY[0]);
    }

    private void minimize() {
        if (root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
            stage.setIconified(true);
        }
    }

    private void launch(MouseEvent event) {
        launchButton.setDisable(true);
        try {
            startLaunch();
        } catch (Exception e) {
            fail("Launch failed: " + e);
        }
    }

    private void startLaunch() throws IOException {
        UserSettings settings = requireSettings();
        Command command = settings == null ? null : createLaunchCommand();
        if (command == null) {
            return;
        }
        String jsonPath = selectedVersionJsonPath(settings);
        if (jsonPath == null) {
            installVersionAndLaunch();
            return;
        }
        String missingVersion = missingClientJarVersion(jsonPath);
        if (missingVersion != null) {
            startInstall(missingVersion);
            return;
        }
        executeLaunch(jsonPath, command);
    }

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

    private UserSettings requireSettings() {
        UserSettings settings = metadata.configurationMapper().userSettings();
        if (settings == null) {
            fail("User settings are missing");
        }
        return settings;
    }

    private Command createLaunchCommand() {
        Command command = Command.create();
        if (addJavaExecutableFileToCommand(command)) {
            return command;
        }
        launchButton.setDisable(false);
        return null;
    }

    private void executeLaunch(String jsonPath, Command command) throws IOException {
        String content = Files.readString(Path.of(jsonPath));
        GameMetadataManifest manifest = Json.GSON.fromJson(content, GameMetadataManifest.class);
        if (!isValidManifest(manifest)) {
            fail("Invalid version JSON: " + jsonPath);
            return;
        }
        Command parsed = parseCommand(content, manifest, command);
        if (parsed == null) {
            fail("Can't parse version JSON: " + jsonPath);
            return;
        }
        startGame(parsed, manifest.id());
    }

    private static boolean isValidManifest(GameMetadataManifest manifest) {
        return manifest != null && manifest.mainClass() != null && !manifest.mainClass().isBlank();
    }

    private Command parseCommand(String content, GameMetadataManifest manifest, Command command) {
        ApplicationConfiguration configuration = metadata.configurationMapper();
        Path gameDirectory = GameDirectory.resolve(configuration);
        Configuration launchConfiguration = LaunchConfiguration.create(
                configuration, manifest, gameDirectory, metadata.features());
        return Parser.create(content, launchConfiguration, metadata.features()).parse(command);
    }

    private void startGame(Command command, String version) {
        SIGNAL_BUS.emit("log.info", LogEvent.of("Starting Minecraft " + version));
        Thread thread = new Thread(() -> runGame(command), "minecraft-launch");
        thread.setDaemon(true);
        thread.start();
    }

    private void runGame(Command command) {
        try {
            command.execute();
        } catch (IOException | InterruptedException e) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("Minecraft exited with error: " + e.getMessage()));
        } finally {
            Platform.runLater(() -> launchButton.setDisable(false));
        }
    }

    private void fail(String message) {
        SIGNAL_BUS.emit("log.error", LogEvent.of(message));
        Platform.runLater(() -> launchButton.setDisable(false));
    }

    private void installVersionAndLaunch() {
        versionManager.enqueue(new ManifestCallback());
    }

    /** 版本清单请求的回调，负责选择并安装版本。 */
    private final class ManifestCallback implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            fail("Network error. Try again: " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {
            try (response) {
                VersionManifest manifest = versionManager.parseVersionManifest(call, response);
                if (manifest == null) {
                    fail("Can't read version manifest");
                    return;
                }
                Platform.runLater(() -> selectAndInstall(manifest));
            }
        }
    }

    private void selectAndInstall(VersionManifest manifest) {
        String version = userSelectVersion(manifest);
        if (version == null) {
            launchButton.setDisable(false);
            return;
        }
        startInstall(version);
    }

    private void startInstall(String version) {
        Thread installer = new Thread(() -> installThenRelaunch(version), "version-install");
        installer.setDaemon(true);
        installer.start();
    }

    private void installThenRelaunch(String version) {
        versionManager.install(version);
        Platform.runLater(() -> afterInstall(version));
    }

    private void afterInstall(String version) {
        List<String> versions = versionManager.listAvailableVersions();
        versionSelector.getItems().setAll(versions);
        versionSelector.setValue(version);
        launchButton.setDisable(false);
        launch(null);
    }

    private boolean addJavaExecutableFileToCommand(Command command) {
        ApplicationConfiguration configuration = metadata.configurationMapper();
        UserSettings settings = configuration.userSettings();
        if (settings == null) {
            fail("User settings are missing");
            return false;
        }
        String javaExecutable = resolveJavaExecutable(configuration, settings);
        if (javaExecutable == null || !Files.isRegularFile(Path.of(javaExecutable))) {
            showJavaPopup(configuration);
            return false;
        }
        command.add(javaExecutable);
        return true;
    }

    private String resolveJavaExecutable(ApplicationConfiguration configuration, UserSettings settings) {
        if (settings.javaHome() != null) {
            return javaExecutableIn(settings.javaHome());
        }
        if (settings.executableJavaCommand() != null) {
            return settings.executableJavaCommand();
        }
        return javaExecutableFromEnvironment(configuration, settings);
    }

    private String javaExecutableFromEnvironment(ApplicationConfiguration configuration, UserSettings settings) {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            return null;
        }
        settings.javaHome(javaHome);
        String executable = javaExecutableIn(javaHome);
        if (executable != null && Files.isRegularFile(Path.of(executable))) {
            configuration.save(ApplicationConfigurationLoader.DEFAULT_PATH);
        }
        return executable;
    }

    private void showJavaPopup(ApplicationConfiguration configuration) {
        SIGNAL_BUS.emit("log.error",
                LogEvent.of("Can't find your java, Do you want to scan your disk or download from Internet for it?"));
        SIGNAL_BUS.emit("ui.popup.confirmation",
                PopupEvent.of("Java",
                        "Do you want to scan for java? Or Download it from internet?",
                        "Click \"Yes\" to scan Java Runtime Environment; \nClick \"No\" to download runtime from internet. (jdk.java.net)",
                        () -> {
                            Environment.scanJavaRuntime(configuration);
                            launch(null);
                        },
                        () -> versionManager.downloadJavaRuntimeFromInternet(configuration),
                        this::noChoice));
    }

    private static String javaExecutableIn(String javaHome) {
        String executable = OperatingSystem.getOs() == OperatingSystem.WINDOWS ? "java.exe" : "java";
        return Path.of(javaHome, "bin", executable).toString();
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
    }

    private String userSelectVersion(VersionManifest manifest) {
        List<String> versions = versionIds(manifest);
        if (versions.isEmpty()) {
            SIGNAL_BUS.emit("log.error", LogEvent.of("No versions available"));
            return null;
        }
        return createVersionDialog(manifest, versions).showAndWait().orElse(null);
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
    }

    private ChoiceDialog<String> createVersionDialog(VersionManifest manifest, List<String> versions) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(initialVersion(manifest, versions), versions);
        if (root.getScene() != null) {
            dialog.initOwner(root.getScene().getWindow());
        }
        dialog.setTitle("Minecraft version");
        dialog.setHeaderText("Select the version to install");
        dialog.setContentText("Version:");
        return dialog;
    }

    private static String initialVersion(VersionManifest manifest, List<String> versions) {
        String initial = manifest.latest() != null && manifest.latest().release() != null
                ? manifest.latest().release()
                : versions.get(0);
        return versions.contains(initial) ? initial : versions.get(0);
    }

    private void noChoice() {
        SIGNAL_BUS.emit("log.error", LogEvent.of("No choice"));
    }
}
