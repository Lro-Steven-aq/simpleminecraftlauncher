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
import java.util.HashMap;
import java.util.Map;

/**
 * 实现加载。
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationPluginLoader implements PluginLoader {
    private final Map<String, ClassLoader> loaderMap;

    public DefaultImplementationPluginLoader() {
        this.loaderMap = new HashMap<>();
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
    }
}
