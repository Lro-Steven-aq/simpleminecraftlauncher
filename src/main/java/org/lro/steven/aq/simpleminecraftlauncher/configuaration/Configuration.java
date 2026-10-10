package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl.DefaultImplementationConfiguration;

import java.util.Map;

/**
 * 处理配置的json的 变量引用 "${abc}"
 * <br/>
 * 比如：${classpath}<br>
 * 其本质就是一个 Map，这是在内部维护的。
 * @author Lro.Steven.aq
 * @since 0.1
 */
public interface Configuration {
    static Configuration create() {
        return new DefaultImplementationConfiguration();
    }

    /**
     * 获取Configuration内部的表.
     * <br>
     * 这是它的本质。<br>
     * @return 其内部维护的表。
     */
    Map<String, String> getMap();

    /**
     * 向内部的Map存储一对K, V.
     * @param key 键
     * @param value 值
     */
    default void add(String key, String value) {
        getMap().put(key, value);
    }

    /**
     * 从内部的Map依据一个K，获取一个 V.
     * @param key 键
     * @return 值
     */
    default String get(String key) {
        return getMap().get(key);
    }

    /**
     * 从内部的Map依据一个K，获取一个 V.
     * @param key 键
     * @return 值
     */
    default String get(String key, String defaultValue) {
        return getMap().getOrDefault(key, defaultValue);
    }
}
