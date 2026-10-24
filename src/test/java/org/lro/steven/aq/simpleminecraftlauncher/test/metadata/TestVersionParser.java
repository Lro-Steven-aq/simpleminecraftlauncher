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
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Command;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Configuration;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Parser;
import org.lro.steven.aq.simpleminecraftlauncher.launch.LaunchConfiguration;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Action;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Artifact;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.GameMetadataManifest;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Library;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestVersionParser {

    @Test
    public void parsesJvmAndGameArguments() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/test-data-version.json"));

        Configuration configuration = Configuration.create();
        configuration.add("natives_directory", "/native");
        configuration.add("classpath", "a.jar" + File.pathSeparator + "b.jar");
        configuration.add("auth_player_name", "Steve");
        configuration.add("version_name", "test-version");

        Command command = Command.create();
        command.add("java");
        Parser parser = Parser.create(json, configuration, Features.create(Map.of()));
        String line = parser.parse(command).getAsString();

        assertTrue(line.contains("-Djava.library.path=/native"), line);
        assertTrue(line.contains("-cp"), line);
        assertTrue(line.contains("a.jar" + File.pathSeparator + "b.jar"), line);
        assertTrue(line.contains("com.example.Main"), line);
        assertTrue(line.contains("--username Steve"), line);
        assertTrue(line.contains("--version test-version"), line);
        assertFalse(line.contains("--demo"), line);
    }

    @Test
    public void buildsClasspathWithRules(@TempDir Path gameDirectory) throws IOException {
        Path versionDirectory = gameDirectory.resolve("versions").resolve("test-version");
        Files.createDirectories(versionDirectory);
        Path clientJar = Files.createFile(versionDirectory.resolve("test-version.jar"));

        Path linuxJar = gameDirectory.resolve("libraries/g/a/1.0/a-1.0.jar");
        Files.createDirectories(linuxJar.getParent());
        Files.createFile(linuxJar);

        Path osxJar = gameDirectory.resolve("libraries/osx/only/1/only-1.jar");
        Files.createDirectories(osxJar.getParent());
        Files.createFile(osxJar);

        Library linuxLibrary = new Library("g:a:1.0",
                new Library.Downloads(new Artifact("g/a/1.0/a-1.0.jar", "x", 1, null, null), null),
                null, null, null);
        Library osxLibrary = new Library("osx:only:1",
                new Library.Downloads(new Artifact("osx/only/1/only-1.jar", "x", 1, null, null), null),
                List.of(new Rule(Action.ALLOW, new Rule.Os(OperatingSystem.OSX, null), null)),
                null, null);

        GameMetadataManifest manifest = new GameMetadataManifest(
                null, null, null, 0, null, "test-version", "test-version", null,
                List.of(linuxLibrary, osxLibrary), null, "com.example.Main", 0, null, null, "release");

        String classpath = LaunchConfiguration.classpath(
                gameDirectory, manifest, clientJar, Features.create(Map.of()));

        assertTrue(classpath.contains(clientJar.toString()), classpath);
        assertTrue(classpath.contains(linuxJar.toString()), classpath);
        assertFalse(classpath.contains(osxJar.toString()), classpath);
    }

    @Test
    public void parsesLibraryNativesObject() {
        String json = """
                {
                  "id": "test",
                  "mainClass": "com.example.Main",
                  "arguments": {"game": []},
                  "libraries": [
                    {
                      "name": "org.lwjgl:lwjgl:3.3.3",
                      "downloads": {
                        "artifact": {
                          "path": "org/lwjgl/lwjgl/3.3.3/lwjgl-3.3.3.jar",
                          "url": "https://example.com/lwjgl.jar",
                          "sha1": "x",
                          "size": 1
                        }
                      },
                      "natives": {"linux": "natives-linux", "windows": "natives-windows"}
                    }
                  ]
                }
                """;

        Command command = Command.create();
        command.add("java");
        Parser parser = Parser.create(json, Configuration.create(), Features.create(Map.of()));
        String line = parser.parse(command).getAsString();

        assertTrue(line.contains("com.example.Main"), line);
    }

    @Test
    public void parsesArgumentWithSingleStringValue() {
        String json = """
                {
                  "id": "test",
                  "mainClass": "com.example.Main",
                  "arguments": {
                    "game": [
                      {"value": "--demo"}
                    ]
                  }
                }
                """;

        Command command = Command.create();
        command.add("java");
        Parser parser = Parser.create(json, Configuration.create(), Features.create(Map.of()));
        String line = parser.parse(command).getAsString();

        assertTrue(line.contains("--demo"), line);
    }
}
