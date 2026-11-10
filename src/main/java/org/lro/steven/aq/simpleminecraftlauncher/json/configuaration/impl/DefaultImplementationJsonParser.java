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
package org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.impl;

import org.lro.steven.aq.simpleminecraftlauncher.json.Json;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Parser;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Argument;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Arguments;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.RuleEvaluator;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实际解析类的默认实现.
 *
 * <p>解析顺序：jvm 参数 → mainClass → game 参数；所有 ${...} 占位符都从
 * {@link Configuration} 里取值。</p>
 *
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationJsonParser implements Parser {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    private Configuration configuration;
    private final String jsonRawContent;
    private final Features features;

    /**
     * 创建版本 JSON 解析器。
     * @param jsonRawContent 参数
     * @param configuration 参数
     * @param features 参数
     */
    public DefaultImplementationJsonParser(String jsonRawContent, Configuration configuration, Features features) {
        this.jsonRawContent = jsonRawContent;
        this.configuration = configuration;
        this.features = features;
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param configuration 参数
     */
    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 实现/覆写接口或父类方法。
     * @return 返回值
     */
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param command 参数
     * @param configuration 参数
     * @return 返回值
     */
    @Override
    public Command parse(Command command, Configuration configuration) {
        GameMetadataManifest manifest = Json.GSON.fromJson(jsonRawContent, GameMetadataManifest.class);
        if (manifest == null) {
            return command;
        }
        Configuration config = effectiveConfiguration(configuration);
        addJvmArguments(command, manifest.arguments(), config);
        addMainClass(command, manifest);
        addGameArguments(command, manifest.arguments(), config);
        return command;
    }

    /**
     * 内部方法：effective Configuration。
     * @param configuration 参数
     * @return 返回值
     */
    private Configuration effectiveConfiguration(Configuration configuration) {
        if (configuration != null) {
            return configuration;
        }
        return this.configuration != null ? this.configuration : Configuration.create();
    }

    /**
     * 添加 Jvm Arguments。
     * @param command 参数
     * @param arguments 参数
     * @param configuration 参数
     */
    private void addJvmArguments(Command command, Arguments arguments, Configuration configuration) {
        if (arguments == null) {
            return;
        }
        addArgumentValues(command, arguments.defaultUserJavaVirtualMachine(), configuration);
        addArgumentValues(command, arguments.javaVirtualMachine(), configuration);
    }

    /**
     * 添加 Game Arguments。
     * @param command 参数
     * @param arguments 参数
     * @param configuration 参数
     */
    private void addGameArguments(Command command, Arguments arguments, Configuration configuration) {
        if (arguments != null) {
            addArgumentValues(command, arguments.game(), configuration);
        }
    }

    /**
     * 添加 Main Class。
     * @param command 参数
     * @param manifest 参数
     */
    private void addMainClass(Command command, GameMetadataManifest manifest) {
        if (manifest.mainClass() != null && !manifest.mainClass().isBlank()) {
            command.add(manifest.mainClass());
        }
    }

    /**
     * 添加 Argument Values。
     * @param command 参数
     * @param arguments 参数
     * @param configuration 参数
     */
    private void addArgumentValues(Command command, List<Argument> arguments, Configuration configuration) {
        if (arguments == null) {
            return;
        }
        for (Argument argument : arguments) {
            if (isApplicable(argument)) {
                addValues(command, argument.value(), configuration);
            }
        }
    }

    /**
     * 判断是否 Applicable。
     * @param argument 参数
     * @return 返回值
     */
    private boolean isApplicable(Argument argument) {
        if (argument == null || argument.value() == null) {
            return false;
        }
        return argument.rules() == null || RuleEvaluator.isAllowed(argument.rules(), features);
    }

    /**
     * 添加 Values。
     * @param command 参数
     * @param values 参数
     * @param configuration 参数
     */
    private void addValues(Command command, List<String> values, Configuration configuration) {
        for (String value : values) {
            command.add(parseReference(value, configuration));
        }
    }

    /**
     * 把字符串里的 ${key} 替换成配置里的值。
     */
    private String parseReference(String string, Configuration configuration) {
        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.find()) {
            return string;
        }
        StringBuffer buffer = new StringBuffer();
        do {
            String replacement = resolveKey(matcher.group(1), matcher.group(0), configuration);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        } while (matcher.find());
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 解析 Key。
     * @param key 参数
     * @param fallback 参数
     * @param configuration 参数
     * @return 返回值
     */
    private String resolveKey(String key, String fallback, Configuration configuration) {
        String value = configuration.get(key);
        if (value != null) {
            return value;
        }
        PluginManager.get().getSignalBus().emit("log.error", LogEvent.of("Unknown KEY: %s".formatted(key)));
        return fallback;
    }

    /**
     * 实现/覆写接口或父类方法。
     * @param command 参数
     * @return 返回值
     */
    @Override
    public Command parse(Command command) {
        return parse(command, configuration);
    }
}
