package org.lro.steven.aq.simpleminecraftlauncher.plugin.loader;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.impl.DefaultImplementationPluginLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * 插件加载接口。<br/>
 * 用来管理{@link PluginClassLoader}类实例
 * @author lro.steven.aq
 * @since 0.1
 */
public interface PluginLoader {

    /**
     * 获取默认实现.
     * @return 默认实现
     */
    static PluginLoader get() {
        return new DefaultImplementationPluginLoader();
    }

    /**
     * 加载一个插件.
     * @param file JAR 文件路径
     * @param clazz 类
     * @return 插件
     * @throws NoSuchMethodException 没有无参的构造函数
     * @throws ClassNotFoundException 没有此类
     * @throws InvocationTargetException 转换失败
     * @throws InstantiationException 实例化失败
     * @throws IllegalAccessException 无权限
     * @throws MalformedURLException 异常URL
     */
    Plugin load(String file, String clazz) throws
            NoSuchMethodException, ClassNotFoundException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException, MalformedURLException;

    /**
     * 卸载一个插件
     * @param file 插件JAR文件路径
     * @throws IOException io异常
     */
    void unload(String file) throws IOException;

    /**
     * 卸载所有插件
     * @throws IOException io异常
     */
    void unloadAll() throws IOException;
}
