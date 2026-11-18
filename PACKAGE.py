#!/usr/bin/env python3
# Copyright (C) ${year} Lro-Steven.aq
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#
# Plugin Exception:
# Plugins that use the package org.lro.steven.aq.simpleminecraftlauncher.plugin.api
# and its subpackages, together with the JAR files they call, are not required
# to be licensed under the GPLv3 and may be distributed under any license
# terms. See the LICENSE file ("SimpleMinecraftLauncher Plugin Exception")
# for the full definition and conditions.

"""Build the release ZIP and tar.gz for SimpleMinecraftLauncher 0.1.

Only the Python standard library is used. Maven and a Java 26 JDK are required
for the build step; use --skip-build to package an existing target/ jar.
"""

from __future__ import annotations

import argparse
import os
import shutil
import subprocess
import sys
import tarfile
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent
DIST = ROOT / "dist"
RELEASE_NAME = "SimpleMinecraftLauncher-0.1"
STAGE_ROOT = DIST / "build" / RELEASE_NAME
APP_JAR_NAME = "simpleminecraftlauncher-0.1.jar"


def maven_command() -> list[str]:
    """Return the Maven wrapper or system Maven command."""
    if os.name == "nt":
        wrapper = ROOT / "mvnw.cmd"
        if wrapper.is_file():
            return ["cmd", "/c", str(wrapper)]
    else:
        wrapper = ROOT / "mvnw"
        if wrapper.is_file():
            if os.access(wrapper, os.X_OK):
                return [str(wrapper)]
            return ["sh", str(wrapper)]

    maven = shutil.which("mvn")
    if maven:
        return [maven]
    raise RuntimeError("Maven was not found in the project root or on PATH.")


def run(command: list[str], cwd: Path = ROOT) -> None:
    """Run a command and fail on a non-zero exit code."""
    print("$", " ".join(command))
    subprocess.run(command, cwd=cwd, check=True)


def newest_app_jar() -> Path:
    """Return the newest application JAR in target/."""
    candidates = [
        path for path in (ROOT / "target").glob("simpleminecraftlauncher-*.jar")
        if not path.name.endswith(("-sources.jar", "-javadoc.jar"))
    ]
    if not candidates:
        raise RuntimeError("target/ does not contain an application JAR.")
    return max(candidates, key=lambda path: path.stat().st_mtime)


def write_readme() -> None:
    """Write the release README next to RUN.py."""
    (STAGE_ROOT / "README.txt").write_text(
        f"""{RELEASE_NAME}

Requirements
------------
- Java 26 or newer
- Python 3.11 or newer

Run
---
    python3 RUN.py

Check only
----------
    python3 RUN.py --check

Layout
------
    RUN.py
    {APP_JAR_NAME}
    lib/                         runtime dependencies
    .simpleminecraftlauncher/    plugins and generated configuration

The launcher creates its configuration on first start:
    .simpleminecraftlauncher/configurations.json

Minecraft game files are not included. Use the launcher to install a version
into the configured game directory.

License
-------
GPL-3.0-or-later with the SimpleMinecraftLauncher Plugin Exception.
See LICENSE and THIRD-PARTY-NOTICES.TXT.
""",
        encoding="utf-8",
    )


def stage_release(skip_build: bool) -> Path:
    """Create the staged release directory."""
    if STAGE_ROOT.exists():
        shutil.rmtree(STAGE_ROOT)
    (STAGE_ROOT / "lib").mkdir(parents=True)

    if not skip_build:
        run(maven_command() + ["-DskipTests", "package"])

    jar = newest_app_jar()
    shutil.copy2(jar, STAGE_ROOT / APP_JAR_NAME)

    run(
        maven_command()
        + [
            "-q",
            "dependency:copy-dependencies",
            "-DincludeScope=runtime",
            f"-DoutputDirectory={STAGE_ROOT / 'lib'}",
        ]
    )

    for name in ("RUN.py", "LICENSE", "THIRD-PARTY-NOTICES.TXT"):
        source = ROOT / name
        if not source.is_file():
            raise RuntimeError(f"missing release file: {source}")
        shutil.copy2(source, STAGE_ROOT / name)

    plugins_source = ROOT / ".simpleminecraftlauncher" / "plugins"
    plugins_target = STAGE_ROOT / ".simpleminecraftlauncher" / "plugins"
    plugins_target.mkdir(parents=True)
    plugin_jars = sorted(plugins_source.glob("*.jar"))
    if not plugin_jars:
        raise RuntimeError(f"no plugin JARs found in: {plugins_source}")
    for plugin in plugin_jars:
        shutil.copy2(plugin, plugins_target / plugin.name)

    write_readme()
    return STAGE_ROOT


def write_zip(stage: Path, target: Path) -> None:
    """Create the release ZIP, preserving executable permissions."""
    with zipfile.ZipFile(target, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for path in sorted(stage.rglob("*")):
            if path.is_dir():
                continue
            arcname = Path(RELEASE_NAME) / path.relative_to(stage)
            info = zipfile.ZipInfo.from_file(path, str(arcname))
            mode = path.stat().st_mode & 0xFFFF
            info.external_attr = mode << 16
            with path.open("rb") as source:
                archive.writestr(info, source.read(), compress_type=zipfile.ZIP_DEFLATED)


def write_tar(stage: Path, target: Path) -> None:
    """Create the release tar.gz."""
    with tarfile.open(target, "w:gz") as archive:
        archive.add(stage, arcname=RELEASE_NAME)


def main(argv: list[str] | None = None) -> int:
    """Build and archive the release."""
    parser = argparse.ArgumentParser(description="Package SimpleMinecraftLauncher 0.1.")
    parser.add_argument(
        "--skip-build",
        action="store_true",
        help="reuse the newest JAR in target/ instead of running Maven package",
    )
    args = parser.parse_args(argv)

    if sys.version_info < (3, 11):
        print("Python 3.11+ is required.", file=sys.stderr)
        return 1

    DIST.mkdir(parents=True, exist_ok=True)
    try:
        stage = stage_release(args.skip_build)
        zip_path = DIST / f"{RELEASE_NAME}.zip"
        tar_path = DIST / f"{RELEASE_NAME}.tar.gz"
        write_zip(stage, zip_path)
        write_tar(stage, tar_path)
    except (OSError, RuntimeError, subprocess.CalledProcessError) as error:
        print(f"error: {error}", file=sys.stderr)
        return 1

    for path in (zip_path, tar_path):
        print(f"{path} ({path.stat().st_size / (1024 * 1024):.1f} MiB)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
