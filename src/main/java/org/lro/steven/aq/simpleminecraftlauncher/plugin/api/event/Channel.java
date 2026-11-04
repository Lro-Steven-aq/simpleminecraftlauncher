package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;

/**
 * 用于 [ 主程序 -> 插件 -> 主程序 ] 之间的通信。
 * <br/>
 * Event仅单向发射，Event<b><i>不应该</i></b>用于双向。<br/>
 * Event<b>应该</b>用完即<b>销毁</b>.
 */
public interface Channel {
    /**
     * 获取发射的信号来源（字符串）
     * @return 信号Signal
     */
    String source();

    /**
     * 发送一个内容.
     * @param content 发送的内容
     */
    void send(Sendable<?> content);

    /**
     * 接收一个内容.
     * @return 接收的内容
     */
    Sendable<?> receive();

}
