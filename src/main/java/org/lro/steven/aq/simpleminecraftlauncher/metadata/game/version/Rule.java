package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import org.jetbrains.annotations.Nullable;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;

import java.util.Map;

public record Rule(Action action, Os os, Map<String, Boolean> features) {

    public record Os(OperatingSystem name, @Nullable VersionRange versionRange) {
    }

}
