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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Channel;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.impl.DefaultImplementationSignalBus;

import java.util.function.Consumer;

/**
 * 这是信号总线接口，用来实现总线.
 * <br/>
 * Default implementation: {@link DefaultImplementationSignalBus}
 * @author lro.steven.aq
 * @since 0.1
 */
public interface SignalBus {

    /**
     * 获取默认实现的类实例.
     * @return 默认实现的类实例
     */
    static SignalBus get() {
        return DefaultImplementationSignalBus.get();
    }

    /**
     * 链接一个信号
     * @param signal 信号
     * @param consumer 触发信号时，所执行的函数
     */
    void connect(String signal, EventConsumer consumer);
    /**
     * 内部方法：connect。
     * @param signal 参数
     * @param consumer 参数
     */
    void connect(String signal, ChannelConsumer consumer);

    /**
     * 发送信号
     * @param signal 信号
     * @param payload 事件对象；可携带附属信息.
     */
    void emit(String signal, Event payload);
    /**
     * 内部方法：emit。
     * @param signal 参数
     * @param payload 参数
     */
    void emit(String signal, Channel payload);

}
