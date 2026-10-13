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
package org.lro.steven.aq.simpleminecraftlauncher.plugin;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.Plugin;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus.SignalBus;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.loader.PluginLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 插件管理类.
 * <br/>
 * 组合起{@code PluginLoader}和{@code SignalBus}
 * @author lro.steven.aq
 * @since 0.1
 */
public class PluginManager {
    private final PluginLoader loader;
    private final SignalBus signalBus;
    private final List<Plugin> plugins;
    private final Map<String, Plugin> pluginMap; // filePath -> Plugin instance

    private boolean started;
    private static PluginManager manager = null;

    public final static String PLUGIN_PATH = "plugins";

    /**
     * 唯一示例化。
     */
    private PluginManager() {
        loader = PluginLoader.get();
        signalBus = SignalBus.get();
        plugins = new ArrayList<>();
        pluginMap = new HashMap<>();
    }
    public static PluginManager get() {
        if (manager == null) {
            manager = new PluginManager();
        }
        return manager;
    }

    /**
     * 加载一个插件
     * @param file JAR文件
     */
    public void start(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String pluginClass = getPluginClass(file);

        try {
            Plugin plugin = loader.load(file.getAbsolutePath(), pluginClass);
            plugins.add(plugin);
            plugin.start(signalBus);
            pluginMap.put(file.getAbsolutePath(), plugin);
        } catch (Throwable throwable) {
            // ignored
            throwable.printStackTrace();
        }
    }


    private Manifest getManifest(File file) throws IOException {
        try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                if ("META-INF/MANIFEST.MF".equals(entry.getName())) {
                    return new Manifest(inputStream);
                }
            }
        }
        return null;
    }

    private String getPluginClass(File file) throws IOException {
        Manifest manifest = getManifest(file);
        if (manifest == null) {
            return null;
        }
        return manifest.getMainAttributes().getValue("Plugin-Class");
    }

    /**
     * 调用所有插件的{@code start(SignalBus signalBus)}方法.
     * <br/>
     * 而且最多执行一次。
     * @throws IOException io错误
     * @throws ClassNotFoundException 查无此类
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 没有无参的构造函数
     * @throws InstantiationException
     * @throws IllegalAccessException 无权限
     */
    public void startAll() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!started) {
            started = true;
            Path dir = Paths.get(PLUGIN_PATH);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir, "*.jar")) {
                for(Path path : directoryStream) {
                    start(path.toFile());
                }
            }
        }
    }

    public SignalBus getSignalBus() {
        return signalBus;
    }

    /**
     * 正序开始逆序卸载。
     * <br/>
     *
     */
    public void stopAll() {
       for(Plugin plugin : plugins.reversed()) {
           try {
               plugin.stop();
           } catch (Throwable throwable) {
               // ignored
               throwable.printStackTrace();
           }
       }
        try {
            loader.unloadAll();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        pluginMap.clear();
    }

    /**
     * 停止一个插件.
     * @param filePath 需要停止的插件路径。
     */
    public void stop(String filePath) {
        String absolutePath = new File(filePath).getAbsolutePath();
        Plugin plugin = pluginMap.get(absolutePath);
        if (plugin == null) {
            throw new IllegalArgumentException("null: %s".formatted(filePath));
        }
        try {
            plugin.stop();
        } catch (Throwable throwable) {
            // ignored
            throwable.printStackTrace();
        }
        try {
            loader.unload(absolutePath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
