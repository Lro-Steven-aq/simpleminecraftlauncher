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
package org.lro.steven.aq.simpleminecraftlauncher.window;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * 让 Windows 对窗口背后的桌面做磨砂。仅影响桌面，不影响启动器自己的界面内容。
 */
public final class DesktopFrost {

    /** WCA_ACCENT_POLICY，见 SetWindowCompositionAttribute。 */
    private static final int WCA_ACCENT_POLICY = 19;
    /**
     * ACCENT_ENABLE_BLURBEHIND。
     * Windows 11 25H2 会静默忽略 ACCENT_ENABLE_ACRYLICBLURBEHIND，但传统 BlurBehind 仍有效。
     */
    private static final int ACCENT_ENABLE_BLURBEHIND = 3;

    private static final SignalBus SIGNAL_BUS = PluginManager.get().getSignalBus();

    private DesktopFrost() {
    }

    /**
     * 对指定标题的窗口开启桌面磨砂。
     * @param windowTitle 窗口标题，用于定位原生窗口句柄
     * @return 磨砂是否已生效；不支持时调用方应改用无磨砂的透明方案
     */
    public static boolean apply(String windowTitle) {
        if (!isWindows()) {
            SIGNAL_BUS.emit("log.info", LogEvent.of("[DesktopFrost] 非 Windows，跳过"));
            return false;
        }
        try (Arena arena = Arena.ofConfined()) {
            Linker linker = Linker.nativeLinker();
            SymbolLookup user32 = SymbolLookup.libraryLookup("user32.dll", arena);
            MethodHandle findWindow = linker.downcallHandle(
                    user32.find("FindWindowA").orElseThrow(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            MethodHandle setComposition = linker.downcallHandle(
                    user32.find("SetWindowCompositionAttribute").orElseThrow(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            MethodHandle getWindowPid = linker.downcallHandle(
                    user32.find("GetWindowThreadProcessId").orElseThrow(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));

            MemorySegment title = arena.allocateFrom(windowTitle);
            MemorySegment hwnd = (MemorySegment) findWindow.invokeExact(MemorySegment.NULL, title);
            if (hwnd.address() == 0) {
                SIGNAL_BUS.emit("log.error", LogEvent.of("[DesktopFrost] 未找到窗口: " + windowTitle));
                return false;
            }
            // 标题可能与其他进程的窗口重名，确认句柄属于当前进程。
            MemorySegment pid = arena.allocate(4, 4);
            int threadId = (int) getWindowPid.invokeExact(hwnd, pid);
            if (pid.get(ValueLayout.JAVA_INT, 0) != (int) ProcessHandle.current().pid()) {
                System.out.println("[DesktopFrost] 窗口不属于当前进程: " + windowTitle);
                return false;
            }

            MemorySegment accent = arena.allocate(16, 4);
            accent.set(ValueLayout.JAVA_INT, 0, ACCENT_ENABLE_BLURBEHIND);
            accent.set(ValueLayout.JAVA_INT, 4, 0);
            accent.set(ValueLayout.JAVA_INT, 8, 0);
            accent.set(ValueLayout.JAVA_INT, 12, 0);

            MemorySegment data = arena.allocate(24, 8);
            data.set(ValueLayout.JAVA_INT, 0, WCA_ACCENT_POLICY);
            data.set(ValueLayout.ADDRESS, 8, accent);
            data.set(ValueLayout.JAVA_LONG, 16, 16L);

            int ret = (int) setComposition.invokeExact(hwnd, data);
            SIGNAL_BUS.emit("log.info", LogEvent.of("[DesktopFrost] applied=" + ret));
            return ret != 0;
        } catch (Throwable error) {
            SIGNAL_BUS.emit("log.fatal", LogEvent.of("[DesktopFrost] failed: " + error));
            // 非 Windows、旧版本 Windows 或未授权原生访问时，由调用方回退到普通透明窗口。
            return false;
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }
}
