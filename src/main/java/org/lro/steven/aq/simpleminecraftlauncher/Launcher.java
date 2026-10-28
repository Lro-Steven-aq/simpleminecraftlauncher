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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lro.steven.aq
 * @since 0.1
 */
public class Launcher {
    static void main(String[] args) {
        // only depend on the java standard library.
        try {
            Application._main(args);
        } catch (Exception e) {
            // 启动失败时必须把根因打印出来，否则只会得到一个退出码，IDE 里看不到任何线索。
            e.printStackTrace();
            System.exit(getExitCode(e));
        }
    }
    private static int getExitCode(Exception e) {
        return switch (e) {
            case IOException ignored -> 1;
            case ClassNotFoundException ignored -> 2;
            case InvocationTargetException ignored -> 3;
            case NoSuchMethodException ignored -> 4;
            case InstantiationException ignored -> 5;
            case IllegalAccessException ignored -> 6;
            case null, default -> -1;
        };
    }
}
