package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.glavo.url.WebURL;

public record File(
    String id,
    String sha1,
    int size,
    WebURL url
) {
}
