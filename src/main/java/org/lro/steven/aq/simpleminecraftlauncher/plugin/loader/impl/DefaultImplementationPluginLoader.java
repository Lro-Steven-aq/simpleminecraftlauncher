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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.impl;

import org.lro.steven.aq.simpleminecraftlauncher.exception.Exception;
import org.lro.steven.aq.simpleminecraftlauncher.exception.plugin.PluginException;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.PluginClassLoader;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.PluginLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现加载。
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationPluginLoader implements PluginLoader {
    private final Map<String, ClassLoader> loaderMap;

    /**
     * 创建插件加载器。
     */
    public DefaultImplementationPluginLoader() {
        this.loaderMap = new ConcurrentHashMap<>();
    }

    @Override
    public Plugin load(String file, String clazz) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        if (!file.endsWith(".jar")) {
            org.lro.steven.aq.simpleminecraftlauncher.exception.Exception exception = new PluginException();
            exception.setMessage("Only JAR File is Supported");
            throw exception;
        }
        if (loaderMap.containsKey(file)) {
            Class<?> cls = loaderMap.get(file).loadClass(clazz);
            return (Plugin) cls.getDeclaredConstructor().newInstance();
        } else {
            URL url = new File(file).toURI().toURL();
            ClassLoader loader = new PluginClassLoader(new URL[]{url}, getClass().getClassLoader());
            loaderMap.put(file, loader);
            Object instance = loader.loadClass(clazz).getDeclaredConstructor().newInstance();

            if (!(instance instanceof Plugin)) {
                Exception exception = new PluginException();
                exception.setMessage("Unimplements org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin interface");
                throw exception;
            }
            return (Plugin) instance;
        }
    }

    @Override
    public void unload(String file) throws IOException {
        PluginClassLoader loader = (PluginClassLoader) loaderMap.remove(file);
        if (loader != null) {
            loader.close();
        }
    }

    @Override
    public void unloadAll() throws IOException {
        for(ClassLoader loader : loaderMap.values()) {
            if (loader != null) {
                PluginClassLoader ploader = (PluginClassLoader) loader;
                ploader.close();
            }
        }
        loaderMap.clear();
    }
}
