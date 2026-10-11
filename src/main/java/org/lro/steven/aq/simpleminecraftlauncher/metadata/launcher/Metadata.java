package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher;

import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;

public final class Metadata {

    private final OperatingSystem system;
    private final String systemVersion;
    private final Features features;

    private Metadata(OperatingSystem system, String systemVersion, Features features) {
        this.system = system;
        this.systemVersion = systemVersion;
        this.features = features;
    }

    public Metadata get() {
        return new Metadata(OperatingSystem.getOs(), OperatingSystem.getVersion(), Features.getInstance());
    }

    public OperatingSystem system() {
        return system;
    }

    public String systemVersion() {
        return systemVersion;
    }

    public Features features() {
        return features;
    }
}
