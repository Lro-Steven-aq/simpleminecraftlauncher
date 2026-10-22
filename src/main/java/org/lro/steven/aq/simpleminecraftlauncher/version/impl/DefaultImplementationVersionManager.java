package org.lro.steven.aq.simpleminecraftlauncher.version.impl;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.glavo.url.WebURL;
import org.jetbrains.annotations.NotNull;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.versionmanifest.VersionManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.version.VersionManager;

import java.util.List;

public class DefaultImplementationVersionManager implements VersionManager {

    private WebURL versionManifestUrl;

    public DefaultImplementationVersionManager(String url) {
        versionManifestUrl = WebURL.parse(url);
    }

    @Override
    public List<String> getVersions() {
        return List.of();
    }

    @Override
    public List<String> listAvailableVersions() {
        return List.of();
    }

    @Override
    public VersionManifest getVersionManifest() {
        return null;
    }

    @Override
    public void install(String version) {

    }

    @Override
    public void enqueue(Callback callback) {

    }

    @Override
    public VersionManifest parseVersionManifest(@NotNull Call call, @NotNull Response response) {
        return null;
    }

    @Override
    public void downloadJavaRuntimeFromInternet(ApplicationConfiguration configuration) {

    }
}
