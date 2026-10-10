/*
    ## WARNING, THIS CODE IS NOT SECURE, AND IT DOES NOT SUPPORT THE OLD VERSION OD MINECRAFT. ##
      WE WILL REFACTOR THIS CLASS IN OTHER BRANCH TO FIX IT.
 */
package org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Parser;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.*;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实际解析类的默认实现.
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationParser implements Parser {
    private static final  Pattern pattern = Pattern.compile("\\$\\{([^}])\\}");

    private Configuration configuration;
    private Command command;
    private String jsonRawContent;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .create();

    public DefaultImplementationParser(String jsonRawContent, Configuration configuration) {
        this.jsonRawContent = jsonRawContent;
        this.command = Command.create();
        this.configuration = configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 解析入口/起点.
     * @param configuration 配置
     * @return 配置好的Command 入口
     */
    @Override
    public Command parse(Configuration configuration) {
        Configuration oldConfiguration = this.configuration;
        this.configuration = configuration;
        Command command = parse();
        this.configuration = oldConfiguration;
        return command;
    }

    /**
     * 以已有配置解析
     * @return 解析结果
     */
    @Override
    public Command parse() {
        // 启动的。
        GameMetadataManifest manifest = gson.fromJson(jsonRawContent, GameMetadataManifest.class);

        if (manifest.arguments().javaVirtualMachine() != null) {
            manifest.arguments().javaVirtualMachine().forEach(argument -> {
                parseRules(argument);
                parseReference(argument);
            });
        }
        if (manifest.arguments().game() != null) {
            manifest.arguments().game().forEach(argument -> {
                parseRules(argument);
                parseReference(argument);
            });
        }

        return command;
    }

    /**
     * 解析所有规则
     * @param argument 参数
     */
    private void parseRules(Argument argument) {
        if (argument == null || argument.rules() == null) {
            return;
        }
        for(Rule rule : argument.rules()) {
            parseRule(rule, argument);
        }
    }

    /**
     * 解析单个规则
     * @param rule 规则
     * @param argument 规则的上下文，主要是需要其{@link Argument#value()}方法
     */
    private void parseRule(Rule rule, Argument argument) {
        if (OperatingSystem.getOs() == rule.os().name() && (rule.os().versionRange() == null || verify(rule.os().versionRange()))) {
            // enable.
            if (rule.action() == Action.ALLOW) {
                // allow.
                command.add((String[]) argument.value().toArray());
            } else if (rule.action() == Action.DISALLOW) {
                // disallow
                PluginManager.get().getSignalBus().emit("log.info", LogEvent.of("Ignore: %s".formatted(argument.toString())));
            }
        } else {
            // ignore
        }

    }

    /**
     * 当前版本是否在{@link VersionRange}里面.
     * <br/>Windows Only
     * @param versionRange 版本范围
     * @return 当前版本是否符合
     */
    private boolean verify(VersionRange versionRange) {
        if (OperatingSystem.getOs() == OperatingSystem.WINDOWS) {

        }
        return true;
    }

    /**
     * 解析参数中的引用：{@code ${classpath}}
     * @param argument
     */
    private void parseReference(Argument argument) {
        if (argument == null || argument.value() == null) {
            return;
        }

            // ${}
        for(String val : argument.value()) {
            Matcher matcher = pattern.matcher(val);
            StringBuffer buffer = new StringBuffer();
            do {
                String key = matcher.group(1);
                String value = configuration.get(key);  // if null, return key itself
                if (value == null) {value = matcher.group(0); }
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(value));
            } while (matcher.find());
            matcher.appendTail(buffer);
            command.add(buffer.toString());
        }
    }
}
