package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import org.glavo.url.WebURL;

public record Client(String sha1, int size, WebURL url) {
}
