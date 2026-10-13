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
 */
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
