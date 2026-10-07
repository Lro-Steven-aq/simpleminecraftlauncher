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