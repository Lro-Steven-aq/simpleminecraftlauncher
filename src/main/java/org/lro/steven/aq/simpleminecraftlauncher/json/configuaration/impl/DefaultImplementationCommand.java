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
package org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.impl;

import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 默认实现的命令运行器
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationCommand implements Command {

    private List<String> command;
    private ExecutorService executor;

    /**
     * 创建一个命令构建器。
     */
    public DefaultImplementationCommand() {
        command = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void execute() throws IOException, InterruptedException {
        SignalBus signalBus = PluginManager.get().getSignalBus();
        ProcessBuilder builder = new ProcessBuilder(command);
        applyLinuxDisplayWorkarounds(builder);
        try (Process process = builder.start()) {
            // Get stdout.
            Process finalProcess = process; // 绕过限制
            CompletableFuture<Void> outputFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(finalProcess.getInputStream(), StandardCharsets.UTF_8))) {
                    String line = null;
                    while ((line = bufferedInputStream.readLine()) != null) {
                        signalBus.emit("log.info", LogEvent.of(line));
                    }
                } catch (IOException e) {
                    signalBus.emit("log.error", LogEvent.of("Error to read `<stdout>`: %s".formatted(e.getMessage())));
                }
            }, executor);

            // Get stderr.
            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line = null;
                    while ((line = bufferedInputStream.readLine()) != null) {
                        signalBus.emit("log.info", LogEvent.of(line));
                    }
                } catch (IOException e) {
                    signalBus.emit("log.error", LogEvent.of("Error to read `<stderr>`: %s".formatted(e.getMessage())));
                }
            }, executor);
            CompletableFuture.allOf(outputFuture, errorFuture);
            int exitcode = process.waitFor();
            if (exitcode != 0) {
                // Panic !
                signalBus.emit("log.error", LogEvent.of("Error: exitcode=%d".formatted(exitcode)));
                signalBus.emit("ui.popup.error",
                        PopupEvent.of(
                                Internationalization.get("launch.error.title"),
                                Internationalization.get("launch.error.head"),
                                Internationalization.get("launch.error.content"),
                                () -> {},
                                () -> {},
                                () -> {}
                        )
                ); // Use I18n
            }
        }
    }

    /**
     * Wayland 下 SDL 窗口没有标题栏和关闭/最小化按钮，强制使用 X11；
     * 同时优先使用 Mesa EGL，规避部分 NVIDIA EGL 驱动崩溃。
     * @param builder 游戏进程构建器
     */
    private static void applyLinuxDisplayWorkarounds(ProcessBuilder builder) {
        if (OperatingSystem.getOs() != OperatingSystem.LINUX
                || System.getenv("WAYLAND_DISPLAY") == null
                || System.getenv("DISPLAY") == null) {
            return;
        }
        Map<String, String> environment = builder.environment();
        environment.putIfAbsent("SDL_VIDEODRIVER", "x11");
        Path mesa = Path.of("/usr/share/glvnd/egl_vendor.d/50_mesa.json");
        if (Files.isRegularFile(mesa)) {
            environment.putIfAbsent("__EGL_VENDOR_LIBRARY_FILENAMES", mesa.toString());
        }
    }

    @Override
    public String getAsString() {
        return String.join(" ", command);
    }

    @Override
    public void add(String... commands) {
        for(String command : commands) {
            this.command.add(command);
        }
    }
}
