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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 启动器支持的操作系统及其版本信息。 */
public enum OperatingSystem {

    /**
     * 内部方法：Serialized Name。
     * @param "linux" 参数
     * @return 返回值
     */
    @SerializedName("osx")
    OSX,

    @SerializedName("linux")
    LINUX,

    @SerializedName("windows")
    WINDOWS;

    private static String VERSION = null;
    private static OperatingSystem CURRENT_SYSTEM = null;

    /**
     * 返回当前操作系统。
     * @return 返回值
     */
    public static OperatingSystem getOs() {
        if (CURRENT_SYSTEM == null) {
            CURRENT_SYSTEM = switch (System.getProperty("os.name").toLowerCase(Locale.ROOT)) {
                case String string when string.contains("win") -> OperatingSystem.WINDOWS;
                case String string when string.contains("mac") -> OperatingSystem.OSX;
                case String string when string.contains("nix") || string.contains("nux") -> OperatingSystem.LINUX;
                case null, default -> null;
            };
        }
        return CURRENT_SYSTEM;

    }

    /**
     * 返回当前操作系统版本。
     * @return 返回值
     */
    public static String getVersion() {
        if (CURRENT_SYSTEM == OperatingSystem.WINDOWS){
            if (VERSION == null) {
                VERSION = "";
                try (Process process = new ProcessBuilder("cmd", "/c", "ver").start();) {
                    String output = new String(process.getInputStream().readAllBytes());
                    Matcher matcher = Pattern.compile("Version\\s+(\\d+(?:\\.\\d+)+)").matcher(output);
                    if (matcher.find()) {
                        VERSION = matcher.group(1);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            VERSION = System.getProperty("os.version");
        }

        return VERSION;
    }
}
