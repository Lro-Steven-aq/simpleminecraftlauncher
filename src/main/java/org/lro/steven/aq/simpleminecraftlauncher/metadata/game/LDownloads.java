package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record LDownloads(
        @Nullable
        Artifact artifact,

        Map<String, Artifact> classifiers
) {
}
