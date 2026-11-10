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
package org.lro.steven.aq.simpleminecraftlauncher.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Window;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.util.List;
import java.util.Optional;

/**
 * 启动器的 UI 弹窗统一入口。
 * <br/>
 * Controller 只负责业务状态，具体的 Alert/ChoiceDialog 和 popup 事件都放在这里。
 */
public final class UIManager {

    /**
     * 创建 {@code UIManager} 实例。
     */
    private UIManager() {
    }

    /**
     * 显示“关于”对话框。
     * @param owner 父窗口，可为 null
     */
    public static void showAbout(Window owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.setTitle("About");
        alert.setHeaderText("SimpleMinecraftLauncher");
        alert.setContentText("""
                A simple Minecraft launcher.

                Version: 0.1-SNAPSHOT
                License: GPLv3 with SimpleMinecraftLauncher Plugin Exception

                Plugins are independent works and may use any license.
                Only run plugins you trust.""");
        alert.showAndWait();
    }

    /**
     * 显示版本选择对话框。
     * @param owner 父窗口，可为 null
     * @param versions 可选版本
     * @param initial 初始版本
     * @param title 标题
     * @param header 头部文本
     * @param content 内容文本
     * @return 用户选择的版本；取消时为空
     */
    public static Optional<String> chooseVersion(Window owner, List<String> versions, String initial,
                                                 String title, String header, String content) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(initial, versions);
        if (owner != null) {
            dialog.initOwner(owner);
        }
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    /**
     * 显示 Java 缺失时的确认弹窗。
     * @param scan 扫描本机 Java
     * @param download 从网络下载 Java
     * @param close 取消/关闭
     */
    public static void showJavaPrompt(Runnable scan, Runnable download, Runnable close) {
        SignalBus signalBus = PluginManager.get().getSignalBus();
        signalBus.emit("log.error",
                LogEvent.of("Can't find your java, Do you want to scan your disk or download from Internet for it?"));
        signalBus.emit("ui.popup.confirmation",
                PopupEvent.of("Java",
                        "Do you want to scan for java? Or Download it from internet?",
                        "Click \"Yes\" to scan Java Runtime Environment; \nClick \"No\" to download runtime from internet. (jdk.java.net)",
                        scan,
                        download,
                        close));
    }
}
