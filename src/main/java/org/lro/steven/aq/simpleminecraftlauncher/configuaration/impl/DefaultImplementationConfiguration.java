package org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置{@code ${var}}的默认实现类.
 * @author lro.steven.aq
 * @since 0.1
 */
public class DefaultImplementationConfiguration implements Configuration {

    private final Map<String, String> configuration;

    public DefaultImplementationConfiguration() {
        configuration = new HashMap<>();
    }

    @Override
    public Map<String, String> getMap() {
        return configuration;
    }
}
