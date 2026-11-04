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
package org.lro.steven.aq.simpleminecraftlauncher.json.configuaration;

import java.io.IOException;
import java.util.List;

/**
 * 一个可执行的命令，它是Parser的解析结果。
 * <br/>
 * 本质是封装了Process/ProcessBuilder
 * @author Lro.Steven.aq
 * @since 0.1
 */
public interface Command {

    /**
     * 创建一个子进程运行命令.
     * @throws IOException 可能是找不到文件？
     * @throws InterruptedException 中断错误。
     */
    void execute() throws IOException, InterruptedException;

    /**
     * 获取为其实际执行的命令行字符串.
     * @return 拼接为命令行的字符串
     */
    String getAsString();

    /**
     * 返回命令参数列表的副本。
     * @return 命令参数列表
     */
    List<String> getCommand();

    /**
     * 追加一条命令参数。
     * @param commands 参数
     */
    void add(String...commands);
}
