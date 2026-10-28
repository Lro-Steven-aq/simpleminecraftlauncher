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
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.controller.impl.LauncherMainWindowController;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.Metadata;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.window.DesktopFrost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/** 启动器主类：加载配置、构建界面并在 JavaFX 中显示主窗口。 */
public class Application extends javafx.application.Application {

    private LauncherMainWindowController launcherController;

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
        Parent root = loader.load();
        if (loader.getController() instanceof LauncherMainWindowController controller) {
            launcherController = controller;
        }
        return root;
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
