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
package org.lro.steven.aq.simpleminecraftlauncher.test.metadata;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.launch.LaunchConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.AssetIndex;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLaunchConfiguration {

    @Test
    public void buildsOfflineAuthAndPaths(@TempDir Path gameDirectory) throws IOException {
        Path versionDirectory = gameDirectory.resolve("versions").resolve("test-version");
        Files.createDirectories(versionDirectory);
        Files.createFile(versionDirectory.resolve("test-version.jar"));
        Path natives = Files.createDirectories(versionDirectory.resolve("natives"));

        GameMetadataManifest manifest = new GameMetadataManifest(
                null,
                new AssetIndex("1", "x", 0, 0, null),
                "1",
                0, null, "test-version", "test-version", null,
                List.of(), null, "com.example.Main", 0, null, null, "release");

        ApplicationConfiguration configuration = new Gson().fromJson(
                "{\"settings\":{\"username\":\"Steve\"}}", ApplicationConfiguration.class);

        Configuration launch = LaunchConfiguration.create(
                configuration, manifest, gameDirectory, Features.create(Map.of()));

        assertEquals("Steve", launch.get("auth_player_name"));
        assertEquals("test-version", launch.get("version_name"));
        assertEquals(gameDirectory.toString(), launch.get("game_directory"));
        assertEquals(natives.toString(), launch.get("natives_directory"));
        assertEquals("1", launch.get("assets_index_name"));
        assertTrue(launch.get("classpath").contains(versionDirectory.resolve("test-version.jar").toString()));
    }
}
