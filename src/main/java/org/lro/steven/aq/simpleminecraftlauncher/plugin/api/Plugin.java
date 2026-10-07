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
