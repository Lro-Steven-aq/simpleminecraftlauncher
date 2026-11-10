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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

import java.util.Objects;

/**
 * 启动事件：只承载启动所需的原始参数，由启动插件负责构建命令并执行。
 * <br/>
 * 启动器发出 {@code launch.fast}/{@code launch.game} 后不再读取事件内容；
 * 启动成功与否、panic 和弹窗都由启动插件负责。
 */
public class LaunchEvent implements Event {

    private final String version;
    private final String versionJson;
    private final Configuration launchConfiguration;
    private final Features features;
    private final String javaExecutable;

    /**
     * 创建 {@code LaunchEvent} 实例。
     * @param version 参数
     * @param versionJson 参数
     * @param launchConfiguration 参数
     * @param features 参数
     * @param javaExecutable 参数
     */
    private LaunchEvent(String version, String versionJson, Configuration launchConfiguration,
                        Features features, String javaExecutable) {
        this.version = version;
        this.versionJson = Objects.requireNonNull(versionJson, "versionJson");
        this.launchConfiguration = Objects.requireNonNull(launchConfiguration, "launchConfiguration");
        this.features = Objects.requireNonNull(features, "features");
        this.javaExecutable = Objects.requireNonNull(javaExecutable, "javaExecutable");
    }

    /**
     * 创建启动事件。
     * @param version 要启动的版本 id
     * @param versionJson 版本 JSON 原文
     * @param launchConfiguration 启动参数配置（classpath、目录、认证等）
     * @param features features 开关
     * @param javaExecutable Java 可执行文件
     * @return 启动事件
     */
    public static LaunchEvent of(String version, String versionJson, Configuration launchConfiguration,
                                 Features features, String javaExecutable) {
        return new LaunchEvent(version, versionJson, launchConfiguration, features, javaExecutable);
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public String source() {
        return "launch event";
    }

    /**
     * 返回要启动的版本 id。
     * @return 版本 id
     */
    public String version() {
        return version;
    }

    /**
     * 返回版本 JSON 原文。
     * @return 版本 JSON
     */
    public String versionJson() {
        return versionJson;
    }

    /**
     * 返回启动参数配置。
     * @return 启动参数配置
     */
    public Configuration launchConfiguration() {
        return launchConfiguration;
    }

    /**
     * 返回 features 开关。
     * @return features
     */
    public Features features() {
        return features;
    }

    /**
     * 返回 Java 可执行文件。
     * @return Java 可执行文件路径
     */
    public String javaExecutable() {
        return javaExecutable;
    }
}
