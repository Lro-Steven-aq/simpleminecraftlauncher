/*
 * Copyright (C) ${year} Lro-Steven.aq
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher;

import javafx.scene.image.Image;
import org.lro.steven.aq.simpleminecraftlauncher.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;

public final class Metadata {

    private final OperatingSystem system;
    private final String systemVersion;
    private final Features features;
    private final Image preciousBackgroundImage;

    private Metadata(OperatingSystem system, String systemVersion, Features features, Image preciousBackgroundImage) {
        this.system = system;
        this.systemVersion = systemVersion;
        this.features = features;
        this.preciousBackgroundImage = preciousBackgroundImage;
    }

    public static Metadata from(Features features, Image preciousBackgroundImage) {
        return new Metadata(OperatingSystem.getOs(), OperatingSystem.getVersion(), features, preciousBackgroundImage);
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

    public Image preciousBackgroundImage() {
        return preciousBackgroundImage;
    }
}
