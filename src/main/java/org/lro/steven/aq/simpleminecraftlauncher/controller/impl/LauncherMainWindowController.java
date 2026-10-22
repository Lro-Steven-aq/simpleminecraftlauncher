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
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.environment.Environment;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Parser;
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
import java.util.ResourceBundle;

public class LauncherMainWindowController extends Controller {

    @FXML
    private ComboBox versionSelector;

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

//    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static final VersionManager VERSION_MANAGER = VersionManager.create();

    public LauncherMainWindowController(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize basic styles and metadata.
        backgroundImage.setImage(metadata.preciousBackgroundImage());

        // 在挂载到 Scene 之前设置整体透明度，避免启动首帧不透明。
        // 这里用节点透明度而不是 Stage.setOpacity：后者在 Linux/GTK 下不会真正改变窗口像素的 alpha。
        UserSettings userSettings = metadata.configurationMapper().userSettings();
        if (userSettings != null && userSettings.alpha() != null) {
            root.setOpacity(userSettings.alpha());
        }
        exitButton.setOpacity(0.7);
        minimizeButton.setOpacity(0.7);
        launchButton.setOpacity(0.7);

        // ////////////////// bind events ///////////////////////
        launchButton.setOnMouseClicked(this::launch);

        // 透明窗口没有拖拽和操作系统的关闭按钮与最小化按钮。
        // 此处需要模拟这些功能.
        final double[] xOffset = new double[1], yOffset = new double[1];
        // 拖拽.
        menuBar.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                Stage stage = (Stage) menuBar.getScene().getWindow();
                xOffset[0] = event.getScreenX() - stage.getX();
                yOffset[0] = event.getScreenY() - stage.getY();
                menuBar.requestFocus();
                event.consume();
            }
        });
        menuBar.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                Stage stage = (Stage) menuBar.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset[0]);
                stage.setY(event.getScreenY() - yOffset[0]);
            }
        });
        exitButton.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        minimizeButton.setOnMouseClicked(event -> {
            // hide it here.
            if (root.getScene() != null && root.getScene().getWindow() instanceof Stage stage) {
                stage.setIconified(true);
            }
        });
    }

    private void launch(MouseEvent event) {

        // build the command.
        Command command = Command.create();
        addJavaExecutableFileToCommand(command);
        boolean hasLastestVersion = false;
        try {
            // 有就直接执行。
            String jsonPath = metadata.configurationMapper().userSettings().lastVersionJsonFile();
            if (jsonPath == null) {
                SIGNAL_BUS.emit("log.warn", LogEvent.of("No version"));
                jsonPath = ".simpleminecraftlauncher/last.json";
            }
            String content = Files.readString(Path.of(jsonPath));
            hasLastestVersion = true;               // should be build //
            Parser parser = Parser.create(content, Configuration.create(), metadata.features());
            parser.parse(command).execute();
        } catch (IOException e) {
            hasLastestVersion = false;
            SIGNAL_BUS.emit("log.error", LogEvent.of("No last version was found. "));
        } catch (InterruptedException e) {
            // 启动失败。
            SIGNAL_BUS.emit("log.error", LogEvent.of("Error to Start.."));
        }

        if (!hasLastestVersion) {
            // popup to let user select a minecraft version, and download it.
            // then, set it to user settings.
            VERSION_MANAGER.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    SIGNAL_BUS.emit("log.fatal", LogEvent.of("Network Error. Try again"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    VersionManifest manifest = VERSION_MANAGER.parseVersionManifest(call, response);
                    // show to user.
                    // select.
                    String version = userSelectVersion(manifest);
                    VERSION_MANAGER.install(version);
                }
            });
        }
    }

    private void addJavaExecutableFileToCommand(Command command) {
        UserSettings userSettings = metadata.configurationMapper().userSettings();
        if (userSettings.javaHome() != null) {
            command.add(userSettings.javaHome() + "/bin");
        } else if (userSettings.executableJavaCommand() != null) {
            command.add(userSettings.executableJavaCommand());
        } else if (userSettings.javaHome() == null && System.getenv().containsKey("JAVA_HOME")) {
            userSettings.javaHome(System.getenv("JAVA_HOME"));
            command.add(userSettings.javaHome() + "/bin");
        } else {
            SIGNAL_BUS.emit("log.error",
                    LogEvent.of("Can't find your java, Do you want to scan your disk or download from Internet for it?"));
            SIGNAL_BUS.emit("ui.popup.confirmation",
                    PopupEvent.of("Java",
                            "Do you want to scan for java? Or Download it from internet?",
                            "Click \"Yes\" to scan Java Runtime Environment; \nClick \"No\" to download runtime from internet. (jdk.java.net)",
                            () -> {Environment.scanJavaRuntime(metadata.configurationMapper());},
                            () -> {VERSION_MANAGER.downloadJavaRuntimeFromInternet(metadata.configurationMapper());},
                            this::noChoice));
            command.add(System.getProperty("JAVA_HOME")); // 这是scanJavaRuntime里面设置的。
            launchButton.setDisable(false);
            return;
        }
    }

    private String userSelectVersion(VersionManifest manifest) {
        // todo
        return null;
    }

    // 附属于launch方法.
    // EMPTY METHOD.
    private void noChoice() {
        SIGNAL_BUS.emit("log.error", LogEvent.of("No choice"));
        // todo: none.
    }
}
