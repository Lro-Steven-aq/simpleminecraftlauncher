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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.downloader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 下载器接口，由下载器插件实现并在启动时注册。
 * <br/>
 * 启动器只依赖这个接口，不直接进行网络下载。
 */
public interface Downloader {

    /**
     * 下载 {@code url} 到 {@code target}。
     * <br/>
     * 目标文件已存在且非空时应跳过；实现应支持基于 {@code .part} 的断点续传。
     * @param url 下载地址
     * @param target 目标文件
     * @throws IOException 下载失败
     */
    void download(String url, Path target) throws IOException;

    /**
     * 读取远程文本内容。
     * @param url 地址
     * @return 文本内容
     * @throws IOException 读取失败
     */
    String readString(String url) throws IOException;
}
