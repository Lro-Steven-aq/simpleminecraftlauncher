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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConfigurationSave {

    @Test
    public void savesWebUrlAsString(@TempDir Path directory) throws IOException {
        Path source = directory.resolve("configurations.json");
        Files.writeString(source, """
                {
                  "configuration_version": "1.0",
                  "settings": {
                    "background_image": "file:/tmp/bg.jpg",
                    "alpha": 0.8
                  }
                }
                """);

        ApplicationConfigurationLoader loader = new ApplicationConfigurationLoader();
        loader.load(source.toFile());

        Path saved = directory.resolve("saved.json");
        loader.getConfigurations().save(saved.toString());
        String content = Files.readString(saved);

        String expectedUrl = org.glavo.url.WebURL.parse("file:/tmp/bg.jpg").toString();
        assertTrue(content.contains(expectedUrl), content);
        assertTrue(content.contains("0.8"), content);

        ApplicationConfigurationLoader reloaded = new ApplicationConfigurationLoader();
        reloaded.load(saved.toFile());
        assertEquals(expectedUrl,
                reloaded.getConfigurations().userSettings().preciousBackgroundImageUrl().toString());
        assertEquals(0.8,
                reloaded.getConfigurations().userSettings().alpha());
    }
}
