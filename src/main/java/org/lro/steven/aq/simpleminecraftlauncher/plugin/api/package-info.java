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
/**
 * 核心：插件系统.
 * <pre>{@code
 * // 比如可能的插件示例：
 * public class TestPlugin implements Plugin {
 *     @Override
 *     public void start(SignalBus signalBus) {
 *         signalBus.connect("launcher.initialize", (e) -> {
 *             IO.println(e.source());
 *         })
 *     }
 * }
 * }</pre>
 * @author lro.steven.aq
 * @since 0.1
 */
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api;
