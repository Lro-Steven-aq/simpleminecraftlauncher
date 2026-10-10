package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import java.util.List;

public record VersionManifest(Latest latest, List<Version> versions) {
}
