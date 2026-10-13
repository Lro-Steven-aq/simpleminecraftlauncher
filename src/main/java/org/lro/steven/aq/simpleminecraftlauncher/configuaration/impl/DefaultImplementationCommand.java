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
package org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.internationalization.Internationalization;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.PopupEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    public DefaultImplementationCommand() {
        command = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void execute() throws IOException, InterruptedException {
        SignalBus signalBus = PluginManager.get().getSignalBus();
        try (Process process = new ProcessBuilder(command) .start()) {
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
                                Internationalization.get("launch.error.content")
                        )
                ); // Use I18n
            }
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
