package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import org.glavo.url.WebURL;

public record Version(String id, VersionType type, WebURL url, String time, String releaseTime) {
}
