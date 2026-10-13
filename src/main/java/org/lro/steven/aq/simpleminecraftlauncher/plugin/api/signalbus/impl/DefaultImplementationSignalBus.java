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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 这是信号总线的默认实现类.
 * <br/>
 * 此后如果需要其他实现请更改{@code SignalBus}的{@code static SignalBus get() {...}} 的指向.
 * @author lro.steven.aq
 * @since 0.1
 */
public final class DefaultImplementationSignalBus implements SignalBus {
    private final Map<String, List<Consumer<Event>>> map;

    /**
     * 拒绝外部实例化
     */
    private DefaultImplementationSignalBus() {
        map = new HashMap<>();
    }

    /**
     * <b>单例化</b>
     */
    private static SignalBus instance = null;
    public static SignalBus get() {
        if (instance == null) {
            instance = new DefaultImplementationSignalBus();
        }
        return instance;
    }

    /// Implement its interface:
    @Override
    public void connect(String signal, Consumer<Event> consumer) {
        map.computeIfAbsent(signal, (string) -> new ArrayList<>()).add(consumer);
    }

    /// Implement its interface:
    @Override
    public void emit(String signal, Event payload) {
        map.getOrDefault(signal, List.of())
                .forEach((e) -> {
                    try {
                        e.accept(payload);
                    } catch (Throwable throwable) {
                        // ignored
                        throwable.printStackTrace();
                    }
                });
    }

}