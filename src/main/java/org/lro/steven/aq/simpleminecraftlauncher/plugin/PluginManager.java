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
 *
 * Plugin Exception:
 * Plugins that use the package org.lro.steven.aq.simpleminecraftlauncher.plugin.api
 * and its subpackages, together with the JAR files they call, are not required
 * to be licensed under the GPLv3 and may be distributed under any license
 * terms. See the LICENSE file ("SimpleMinecraftLauncher Plugin Exception")
 * for the full definition and conditions.
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
import java.util.stream.Stream;
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

    public final static String PLUGIN_PATH = ".simpleminecraftlauncher/plugins";

    /**
     * 唯一示例化。
     */
    private PluginManager() {
        loader = PluginLoader.get();
        signalBus = SignalBus.get();
        plugins = new ArrayList<>();
        pluginMap = new HashMap<>();
    }
    /**
     * 返回插件管理器单例。
     * @return 返回值
     */
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


    /**
     * 获取 Manifest。
     * @param file 参数
     * @return 返回值
     * @throws IOException 异常
     */
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

    /**
     * 获取 Plugin Class。
     * @param file 参数
     * @return 返回值
     * @throws IOException 异常
     */
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
    public void startAll() throws IOException {
        if (!started) {
            started = true;
            Path dir = Paths.get(PLUGIN_PATH);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            try(Stream<Path> directoryStream = Files.list(dir)) {
                directoryStream.filter(path -> path.toString().endsWith(".jar"))
                        .sorted((path1, path2) -> {
                            // logger 优先，不管它是谁实现的logger.
                            // 因为其他的插件可能在.start方法里就依赖于plugin-logger 了。
                            // 如果需要额外的优先，应该插入在logger后面(见 <位置1>)
                            boolean isLogger1 = path1.getFileName().toString().toLowerCase(Locale.ROOT).contains("logger");
                            boolean isLogger2 = path2.getFileName().toString().toLowerCase(Locale.ROOT).contains("logger");
                            if (isLogger1 && !isLogger2) {
                                // path1 放在前面
                                return -1;
                            }
                            if (!isLogger1 && isLogger2) {
                                // path2 放在前面.
                                return 1;
                            }
                            // <位置1>，应该此处插入此后需要优先的插件。
                            return path1.compareTo(path2);
                        })
                        .forEach(path -> {
                            try {
                                start(path.toFile());
                            }  catch (Throwable throwable) {
                                // skip it.
                                throwable.printStackTrace();
                            }
                        });
            }
        }
    }

    /**
     * 返回全局信号总线。
     * @return 返回值
     */
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
