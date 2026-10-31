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

import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.impl.DefaultImplementationJsonParser;

/**
 * Parser类是用于解析参数json的接口.
 * <br/>
 * 所有适于解析的类都应该实现它。<br>
 * 关于其parse返回值 {@link Command}
 * @author Lro.Steven.aq
 * @since 0.1
 *
 */
public interface Parser {

    /**
     * 创建版本 JSON 参数解析器。
     * @param jsonRawContent 版本 JSON 文本
     * @param configuration 参数配置
     * @param features features 开关
     * @return 解析器
     */
    static Parser create(String jsonRawContent, Configuration configuration, Features features) {
        return new DefaultImplementationJsonParser(jsonRawContent, configuration, features);
    }

    /**
     * 将配置设置为该参数.
     * <br>
     * @param configuration 将要的设置
     */
    void setConfiguration(Configuration configuration);

    /**
     * 用于获取其当前的配置.
     * <br>
     * @return 其现在所有的配置
     */
    Configuration getConfiguration();

    /**
     * 按照给定的配置来解析而忽略已经设置的配置来解析.
     * <br/>
     * 注意：不覆盖字段，只是不使用字段的configuration。<br/>
     * @param configuration 配置
     * @return 配置结束的命令
     */
    Command parse(Command command, Configuration configuration);

    /**
     * 使用已有的配置解析，返回一个可执行的命令.
     * <br>
     * 其内部委托于{@link #parse(Command, Configuration)}
     * @return 配置好的命令
     */
    Command parse(Command command);
}
