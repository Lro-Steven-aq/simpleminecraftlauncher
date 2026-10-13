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
