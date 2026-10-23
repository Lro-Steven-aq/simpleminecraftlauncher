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

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

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

    private Configuration effectiveConfiguration(Configuration configuration) {
        if (configuration != null) {
            return configuration;
        }
        return this.configuration != null ? this.configuration : Configuration.create();
    }

    private void addJvmArguments(Command command, Arguments arguments, Configuration configuration) {
        if (arguments == null) {
            return;
        }
        addArgumentValues(command, arguments.defaultUserJavaVirtualMachine(), configuration);
        addArgumentValues(command, arguments.javaVirtualMachine(), configuration);
    }

    private void addGameArguments(Command command, Arguments arguments, Configuration configuration) {
        if (arguments != null) {
            addArgumentValues(command, arguments.game(), configuration);
        }
    }

    private void addMainClass(Command command, GameMetadataManifest manifest) {
        if (manifest.mainClass() != null && !manifest.mainClass().isBlank()) {
            command.add(manifest.mainClass());
        }
    }

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

    private boolean isApplicable(Argument argument) {
        if (argument == null || argument.value() == null) {
            return false;
        }
        return argument.rules() == null || RuleEvaluator.isAllowed(argument.rules(), features);
    }

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

    private String resolveKey(String key, String fallback, Configuration configuration) {
        String value = configuration.get(key);
        if (value != null) {
            return value;
        }
        PluginManager.get().getSignalBus().emit("log.error", LogEvent.of("Unknown KEY: %s".formatted(key)));
        return fallback;
    }

    @Override
    public Command parse(Command command) {
        return parse(command, configuration);
    }
}
