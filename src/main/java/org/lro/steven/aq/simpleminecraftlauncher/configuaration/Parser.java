package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl.DefaultImplementationParser;

/**
 * Parser类是用于解析参数json的接口.
 * <br/>
 * 所有适于解析的类都应该实现它。<br>
 * 关于其parse返回值 {@link org.lro.steven.aq.simpleminecraftlauncher.configuaration.Command}
 * @author Lro.Steven.aq
 * @since 0.1
 *
 */
public interface Parser {

    static Parser create(String jsonRawContent, Configuration configuration) {
        return new DefaultImplementationParser(jsonRawContent, configuration);
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
     * 如果需要使用已设置的配置，请使用{@link #parse()}<br/>
     * 注意：不覆盖字段，只是不使用字段的configuration。<br/>
     * @param configuration 配置
     * @return 配置结束的命令
     */
    Command parse(Configuration configuration);

    /**
     * 使用已有的配置解析，返回一个可执行的命令.
     * <br>
     * @return 配置好的命令
     */
    Command parse();
}
