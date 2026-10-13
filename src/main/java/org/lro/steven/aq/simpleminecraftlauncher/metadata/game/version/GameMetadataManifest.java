package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record GameMetadataManifest(
        Arguments arguments,
        AssetIndex assetIndex,

        @SerializedName("assets")
        String assetsIndex,
        int complianceLevel,
        Downloads downloads,
        String id,
        JavaVersion javaVersion,
        List<Library> libraries,
        Logging logging,
        String mainClass,
        int minimumLauncherVersion,
        String releaseTime,
        String time,
        String type
) {
}
