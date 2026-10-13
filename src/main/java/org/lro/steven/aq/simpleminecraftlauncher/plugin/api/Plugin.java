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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

/**
 * 开发者的插件接口.
 * <br/>
 * 所有插件都应该实现它
 * @author lro.steven.aq
 * @since 0.1
 */
public interface Plugin {

    /**
     * 在开始时调用；应该链接信号.
     * <pre>{@code
     * public void start(SignalBus signalBus) {
     *     signalBus.connect("launcher.init", (e) -> {
     *         IO.println(e.source());
     *     });
     * }
     * }</pre>
     * @param signalBus 应该运用的信号总线。
     */
    void start(SignalBus signalBus);

    /**
     * 默认不会在结束时做任何事.
     * <br/>
     * 如果需要，请覆写它：
     * <pre>{@code
     * public void stop() {
     *     // do some cleanup.
     *     // for examples: fstream.close();
     * }
     * }</pre>
     */
    default void stop() {
    }
}
