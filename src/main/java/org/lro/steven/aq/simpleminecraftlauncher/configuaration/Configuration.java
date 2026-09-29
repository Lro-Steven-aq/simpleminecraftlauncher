package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

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

    /**
     * 获取Configuration内部的表.
     * <br>
     * 这是它的本质。<br>
     * @return 其内部维护的表。
     */
    Map<String, String> getMap();
}
