package org.lro.steven.aq.simpleminecraftlauncher.version;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.version.impl.DefaultImplementationVersionManager;

import java.util.List;

public interface VersionManager {
    static VersionManager create() {
        return new DefaultImplementationVersionManager("https://piston-meta.mojang.com/mc/game/version_manifest.json");
    }


    List<String> getVersions();
    List<String> listAvailableVersions();
    VersionManifest getVersionManifest();
    void install(String version);
    void enqueue(Callback callback);
    VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response);
    void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration);
}
