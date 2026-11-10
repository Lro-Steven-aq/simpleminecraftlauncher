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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

/** 日志事件。 */
public class LogEvent implements Event {
    private final String message;
    /**
     * 创建 {@code LogEvent} 实例。
     * @param message 参数
     */
    private LogEvent(String message) {
        this.message = message;
    }

    /**
     * 创建一条日志事件。
     * @param message 参数
     * @return 返回值
     */
    public static Event of(String message) {
        return new LogEvent(message);
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public String source() {
        return "logger event";
    }

    /**
     * 获取 Message。
     * @return 返回值
     */
    public String getMessage() {
        return message;
    }
}
