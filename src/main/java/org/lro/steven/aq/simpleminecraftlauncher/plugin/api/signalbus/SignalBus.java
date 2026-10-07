package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.impl.DefaultImplementationSignalBus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
    void connect(String signal, Consumer<Event> consumer);

    /**
     * 发送信号
     * @param signal 信号
     * @param payload 事件对象；可携带附属信息.
     */
    void emit(String signal, Event payload);

}
