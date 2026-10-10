package org.lro.steven.aq.simpleminecraftlauncher.metadata.game;

import com.google.gson.annotations.SerializedName;

public enum VersionType {

    @SerializedName("release")
    RELEASE,

    @SerializedName("snapshot")
    SNAPSHOT,

    @SerializedName("old_beta")
    OLD_BETA,

    @SerializedName("old_alpha")
    OLD_ALPHA,
}
