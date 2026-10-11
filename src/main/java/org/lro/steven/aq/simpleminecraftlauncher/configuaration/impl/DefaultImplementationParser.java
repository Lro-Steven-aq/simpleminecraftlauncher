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
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Parser;
import org.lro.steven.aq.simpleminecraftlauncher.deserializer.WebURLDeserializer;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.*;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 实际解析类的默认实现.
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationParser implements Parser {
    private static final  Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WebURL.class, new WebURLDeserializer())
            .create();
    private static final OperatingSystem CURRENT_SYSTEM = OperatingSystem.getOs();

    private Configuration configuration;
    private final String jsonRawContent;


    public DefaultImplementationParser(String jsonRawContent, Configuration configuration) {
        this.jsonRawContent = jsonRawContent;
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
        Command command = Command.create();

        GameMetadataManifest manifest = GSON.fromJson(jsonRawContent, GameMetadataManifest.class);
        if (manifest == null || manifest.arguments() == null) {
            return null;
        }
        for (Argument argument : manifest.arguments().game()) {
            if (argument.value() == null) {
                continue;
            }

            if (argument.rules() == null || isAllowed(argument.rules())) {
                argument.value().forEach((v) -> command.add(parseReference(v, configuration)));
            } else {
                // ignore it
            }
        }
        // then: process "jvm"
        return command;
    }

    /**
     * 测试本机是否符合规则.
     * <br/>
     * 后面可覆盖前面的.
     * @param rules 规则
     * @return 本机是否符合
     */
    private boolean isAllowed(List<Rule> rules) {
        boolean allowed = false;    // 默认禁用.
        if (rules == null || rules.isEmpty()) {
            return true;
        }
        if (rules.stream().anyMatch(Objects::isNull)) {
            return false;
        }
        for (Rule rule : rules) {
            if (matches(rule)) {
                allowed = (rule.action() == Action.ALLOW);
            }
        }

        return allowed;
    }

    /**
     * 测试本机是否符合规则.
     *
     * @param rule 规则
     * @return 本机是否符合
     */
    private boolean matches(Rule rule) {
        if (rule.os() != null) {
            Os os = rule.os();
            if (os.name() == null || os.name() != CURRENT_SYSTEM) {
                return false;
            }
            if (os.versionRange() != null && !checkVersionRange(os.versionRange())) {
                return false;
            }
        }

        if (rule.features() != null) {
            for (Map.Entry<String, Boolean> entry : rule.features().entrySet()) {
                if (! Features
                        .getInstance()
                        .getOrDefault(
                                entry.getKey(),
                                false).equals(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;  // 没有被 Guard Condition 拦截, 则认为该Rule匹配.
    }

    /**
     * 判断是不是在版本范围内
     * @param versionRange 需要的系统版本范围
     * @return 当前系统是否在这个范围内
     */
    private boolean checkVersionRange(VersionRange versionRange) {
        if (versionRange == null) {
            return true;
        }
        if (CURRENT_SYSTEM != OperatingSystem.WINDOWS) {
            return true;
        }
        String[] currentVersion = OperatingSystem.getVersion().split("\\.");
        if (versionRange.max() != null && isGreaterThan(currentVersion, versionRange.max().split("\\."))) {
            return false;
        }

        if (versionRange.min() != null && ! isGreaterThan(currentVersion, versionRange.min().split("\\."))) {
            return false;
        }
//        String max = versionRange.max();
//        String min = versionRange.min();
//        String[] maxVersion = max.split("\\.");
//        String[] minVersion = min.split("\\.");
        /*
            min 1.0.2.8.12345
            max 2.3.4.0.00112
            current 1.123.456.78900
         */
        return true;
    }

    /**
     * 按照版本号的规则:
     * <br/>
     * Is a greater than b?
     * @param a 应该是CURRENT_VERSION
     * @param b MAX_VERSION或 MIN_VERSION
     * @return a是否b大于？
     */
    private boolean isGreaterThan(String[] a, String[] b) {
        int length = Math.max(a.length, b.length);
        for (int i = 0; i < length; i++) {
            String segmentA = i < a.length ? a[i] : "0";
            String segmentB = i < b.length ? b[i] : "0";
            int diff = Integer.compare(stringToInteger(segmentA), stringToInteger(segmentB));
            if (diff < 0) {
                return false;
            } else if (diff > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 尝试 字符串转数字，若出错，则使用0作为默认值.
     * @param string 输入的字符串
     * @return 数字
     */
    private int stringToInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    /**
     *
     * @param string 要进行处理的字符串
     * @return 处理后的字符串
     */
    private String parseReference(String string, Configuration configuration) {
        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.find()) {
            return string;
        }
        StringBuffer buffer = new StringBuffer();
        do {
            String key = matcher.group(1);
            String value = configuration.get(key);
            if (value == null) {
                PluginManager.get().getSignalBus().emit("log.error", LogEvent.of("Unknown KEY: %s".formatted(key)));
                value = matcher.group(0);
            }
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(value));
        } while (matcher.find());

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 以已有配置解析
     * @return 解析结果
     */
    @Override
    public Command parse() {
        return parse(this.configuration);
    }

}
