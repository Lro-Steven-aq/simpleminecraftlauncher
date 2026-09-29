package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

import java.io.IOException;

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
}
