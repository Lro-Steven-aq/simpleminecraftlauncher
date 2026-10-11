package org.lro.steven.aq.simpleminecraftlauncher.configuaration;

import java.util.HashMap;
import java.util.Map;

public final class Features {
    private Map<String, Boolean> map;
    private static Features features = null;


    public static Features getInstance() {
        if (features == null) {
            features = new Features();
            features.map = new HashMap<>();
        }
        return features;
    }

    private Features() {}

    public Boolean getOrDefault(String key, Boolean defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

}
