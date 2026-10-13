package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest;

import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.VersionType;

public record Version(String id, VersionType type, WebURL url, String time, String releaseTime) {
}
