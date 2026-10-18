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

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.lro.steven.aq.simpleminecraftlauncher.controller.Controller;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Parser;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.settings.UserSettings;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.net.URL;
import java.util.ResourceBundle;

public class LauncherMainWindowController extends Controller {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button launchButton;

    @FXML
    private Pane root;

    private final Metadata metadata;

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();

    public LauncherMainWindowController(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOpacity(0.6);
        // Internationalization
        backgroundImage.setImage(metadata.preciousBackgroundImage());
        launchButton.setOnMouseClicked(this::launch);

    }

    private void launch(MouseEvent event) {
//        Request request = new Request.Builder()
//                .url()
        IO.println("//////////////////");
        launchButton.setDisable(true);
        root.setOpacity(0.65);

        Command command = Command.create();
        UserSettings userSettings = metadata.configurationMapper().userSettings();
        if (userSettings.javaHome() != null) {
            command.add(userSettings.javaHome());
        } else if (userSettings.executableJavaCommand() != null) {
            command.add(userSettings.executableJavaCommand());
        } else {
            SIGNAL_BUS.emit("log.error",
                    LogEvent.of("Can't find your java, Do you want to scan your disk or download from Internet for it?"));
            SIGNAL_BUS.emit("ui.popup.confirmation",
                    PopupEvent.of("Java",
                            "Do you want to scan for java? Or Download it from internet?",
                            "Click \"Yes\" to scan Java Runtime Environment; \nClick \"No\" to download runtime from internet. (jdk.java.net)",
                            this::scanJavaRuntime,
                            this::downloadJavaRuntimeFromInternet,
                            this::noChoice));
            launchButton.setDisable(false);
        }

    }

    private void scanJavaRuntime() {
        SIGNAL_BUS.emit("log.info", LogEvent.of("User clicks OK button"));
        // todo
    }

    private void downloadJavaRuntimeFromInternet() {
        SIGNAL_BUS.emit("log.error", LogEvent.of("User click to SCAN DISK"));
        // todo
    }

    private void noChoice() {
        SIGNAL_BUS.emit("log.error", LogEvent.of("No choice"));
        // todo
    }
}
