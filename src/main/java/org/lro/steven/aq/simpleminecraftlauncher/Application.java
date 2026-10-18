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
package org.lro.steven.aq.simpleminecraftlauncher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.controller.impl.LauncherMainWindowController;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load config -> preciousBackgroundImage -> Metadata
        ApplicationConfigurationLoader conf = loadConfiguration();
        ApplicationConfiguration configurations = conf.getConfigurations();
        Metadata metadata = Metadata.from(Features.create(new HashMap<>()), configurations);
        // size: 577 x 900

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Application.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass.equals(LauncherMainWindowController.class)) {
                return new LauncherMainWindowController(metadata);
            } else {
                try { return controllerClass.getDeclaredConstructor().newInstance(); }
                catch (Exception e) {throw new RuntimeException(e); }
            }
        });

        Parent root = fxmlLoader.load();

        Font.loadFont(getClass().getResourceAsStream("LXGWWenKaiMono-Regular.ttf"), 48);

        primaryStage.setTitle("Simple Launcher");

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root);
        // 透明窗口必须在 show() 之前设置场景背景透明，否则第一帧会是不透明白底。
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void _main(String[] args) throws IOException {
        // start signalBus to load the plugins.
        PluginManager manager = PluginManager.get();

        manager.startAll();
        manager.getSignalBus().emit("log.info", LogEvent.of("initialize"));
        // ///////////////////////////////////
        javafx.application.Application.launch(Application.class, args);
    }

    public static ApplicationConfigurationLoader loadConfiguration() {
        ApplicationConfigurationLoader configuration = new ApplicationConfigurationLoader();
        configuration.load(new File(".simpleminecraftlauncher/configurations.json"));
        return configuration;
    }
}
