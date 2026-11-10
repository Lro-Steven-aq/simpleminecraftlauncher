package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event;

import java.io.Serializable;

/**
 * 表示 {@code Sendable}。
 */
public interface Sendable<V> extends Serializable {

    /**
     * 发送的内容
     * @return 内容
     */
    V get();
}
