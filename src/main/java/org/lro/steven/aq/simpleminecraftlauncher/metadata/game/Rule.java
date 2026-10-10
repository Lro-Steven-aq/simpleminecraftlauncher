package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import java.util.Map;

public record Rule(Action action, Os os, Map<String, Boolean> features) {
}
