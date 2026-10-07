package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;

/**
 * 所有事件的接口.
 * <br/>
 * 在SignalBus里面传入的就是它。
 *
 * @author lro.steven.aq
 * @since 0.1
 */
public interface Event {

    /**
     * 获取发射的信号来源（字符串）
     * @return 信号Signal
     */
    String source();
}
