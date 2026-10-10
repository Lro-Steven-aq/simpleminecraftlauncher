package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.glavo.url.WebURL;

public record AssetIndex(String id, String sha1, int size, int totalSize, WebURL url) {
}
