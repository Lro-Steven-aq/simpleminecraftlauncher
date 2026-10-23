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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 插件类加载器：核心类向父类请求，私有类子类优先。
 * @author lro.steven.aq
 * @since 0.1
 */
public class PluginClassLoader extends URLClassLoader {
    /**
     * 应当向父类请求的类限定名的前缀。
     */
    private static final List<String> P = List.of(
            "java.", "javax.", "jdk.", "sun.",
            "org.lro.steven.aq."
    );

    /**
     * 创建插件类加载器。
     * @param urls 参数
     * @param parent 参数
     */
    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * 加载类，若其前缀在{@code P}里面，则委托于父类；否然子类加载.
     * @param name
     *          The <a href="#binary-name">binary name</a> of the class
     *
     * @return 找到的类
     * @throws ClassNotFoundException 类未找到
     */
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for(String prefix : P) {
            if (name.startsWith(prefix)) {
                return super.loadClass(name, resolve);
            }
        }
        synchronized (getClassLoadingLock(name)) {
            Class<?> clazz = findLoadedClass(name);
            if (clazz == null) {
                try {
                    clazz = findClass(name);
                } catch (ClassNotFoundException e) {
                    clazz = super.loadClass(name, resolve);
                }
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }
}
