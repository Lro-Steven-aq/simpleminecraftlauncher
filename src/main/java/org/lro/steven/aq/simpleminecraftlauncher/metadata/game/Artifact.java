package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.glavo.url.WebURL;
import org.jetbrains.annotations.Nullable;

public record Artifact(
        String path,
        String sha1,
        long size,
        WebURL url,

        @Nullable
        String sha512
) {
}
