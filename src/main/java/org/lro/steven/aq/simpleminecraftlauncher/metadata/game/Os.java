package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.jetbrains.annotations.Nullable;

public record Os(OperatingSystem name, @Nullable VersionRange versionRange) {
}
