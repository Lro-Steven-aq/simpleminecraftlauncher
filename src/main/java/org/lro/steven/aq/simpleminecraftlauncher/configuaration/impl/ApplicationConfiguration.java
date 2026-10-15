package org.lro.steven.aq.simpleminecraftlauncher.configuaration.impl;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfiguration implements Configuration {

    private Map<String, String> map;

    public ApplicationConfiguration() {
        this.map = new HashMap<>();
    }

    public void load(String path) {
     // unimplement..
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }
}
