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

/** 启动器主类：加载配置、构建界面并在 JavaFX 中显示主窗口。 */
public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationConfiguration configurations = loadConfiguration().getConfigurations();
        Metadata metadata = Metadata.from(Features.create(new HashMap<>()), configurations);
        Parent root = loadRoot(metadata);
        Font.loadFont(getClass().getResourceAsStream("LXGWWenKaiMono-Regular.ttf"), 48);
        showStage(primaryStage, root);
    }

    private Parent loadRoot(Metadata metadata) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
        loader.setControllerFactory(controllerClass -> createController(controllerClass, metadata));
        return loader.load();
    }

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

    private void showStage(Stage stage, Parent root) {
        stage.setTitle("Simple Launcher");
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * JavaFX 入口：启动插件系统并启动应用。
     * @param args 参数
     * @throws IOException 异常
     */
    public static void _main(String[] args) throws IOException {
        // start signalBus to load the plugins.
        PluginManager manager = PluginManager.get();

        manager.startAll();
        manager.getSignalBus().emit("log.info", LogEvent.of("initialize"));
        // ///////////////////////////////////
        javafx.application.Application.launch(Application.class, args);
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
}
