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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 下载事件：既表示“下载文件”，也表示“读取文本”。
 * <br/>
 * 启动器发出 {@code download.file}/{@code download.read} 信号，下载器插件处理
 * 后把结果写回事件。
 */
public class DownloadEvent implements Event {

    private final String url;
    private final Path target;
    private String content;
    private IOException error;
    private boolean handled;

    private DownloadEvent(String url, Path target) {
        this.url = url;
        this.target = target;
    }

    /**
     * 创建文件下载事件。
     * @param url 下载地址
     * @param target 目标文件
     * @return 下载事件
     */
    public static DownloadEvent of(String url, Path target) {
        return new DownloadEvent(url, target);
    }

    /**
     * 创建文本读取事件。
     * @param url 地址
     * @return 下载事件
     */
    public static DownloadEvent of(String url) {
        return new DownloadEvent(url, null);
    }

    @Override
    public String source() {
        return "download event";
    }

    /**
     * 返回下载地址。
     * @return 下载地址
     */
    public String url() {
        return url;
    }

    /**
     * 返回目标文件。
     * @return 目标文件；文本读取事件为 null
     */
    public Path target() {
        return target;
    }

    /**
     * 返回读取到的文本。
     * @return 文本；尚未读取时为 null
     */
    public String content() {
        return content;
    }

    /**
     * 设置读取到的文本。
     * @param content 文本
     */
    public void content(String content) {
        this.content = content;
    }

    /**
     * 返回下载/读取错误。
     * @return 错误；成功时为 null
     */
    public IOException error() {
        return error;
    }

    /**
     * 记录下载/读取失败。
     * @param error 错误
     */
    public void fail(IOException error) {
        this.error = error;
    }

    /**
     * 返回事件是否已被插件处理。
     * @return 是否已处理
     */
    public boolean handled() {
        return handled;
    }

    /**
     * 标记事件已被处理。
     * @param handled 是否已处理
     */
    public void handled(boolean handled) {
        this.handled = handled;
    }

    /**
     * 如果下载/读取失败，抛出对应异常。
     * @throws IOException 下载/读取失败
     */
    public void throwIfFailed() throws IOException {
        if (error != null) {
            throw error;
        }
    }
}
