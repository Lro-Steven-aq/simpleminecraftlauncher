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

"""Check JAVA_HOME and start SimpleMinecraftLauncher.

Only the Python standard library is used. Python 3.11 or newer is required.
"""

from __future__ import annotations

import argparse
import os
import re
import shutil
import subprocess
import sys
from pathlib import Path

MIN_PYTHON = (3, 11)
MIN_JAVA = 26
PROJECT_ROOT = Path(__file__).resolve().parent


def java_binary(home: Path) -> Path:
    """Return the java executable inside a JDK home."""
    name = "java.exe" if os.name == "nt" else "java"
    return home / "bin" / name


def javac_binary(home: Path) -> Path:
    """Return the javac executable inside a JDK home."""
    name = "javac.exe" if os.name == "nt" else "javac"
    return home / "bin" / name


def java_major(java: Path) -> int | None:
    """Read the major version from ``java -version``."""
    try:
        result = subprocess.run(
            [str(java), "-version"],
            capture_output=True,
            text=True,
            check=False,
        )
    except OSError:
        return None
    output = (result.stdout or "") + (result.stderr or "")
    match = re.search(r'version "(\d+)', output)
    return int(match.group(1)) if match else None


def java_home_from_java(java: Path) -> Path | None:
    """Derive java.home from a java executable."""
    try:
        result = subprocess.run(
            [str(java), "-XshowSettings:properties", "-version"],
            capture_output=True,
            text=True,
            check=False,
        )
    except OSError:
        return None
    output = (result.stdout or "") + (result.stderr or "")
    match = re.search(r"^\s*java\.home = (.+)$", output, re.MULTILINE)
    if not match:
        return None
    return Path(match.group(1).strip())


def inspect_home(home: Path) -> tuple[Path, int] | str:
    """Validate a JDK home and return (home, major) or an error message."""
    home = home.expanduser().resolve()
    java = java_binary(home)
    if not java.is_file():
        return f"{home} does not contain bin/{java.name}"
    if not javac_binary(home).is_file():
        return f"{home} is a JRE, but a JDK is required (javac is missing)"

    major = java_major(java)
    if major is None:
        return f"could not determine the Java version from {java}"
    if major < MIN_JAVA:
        return f"Java {major} found at {java}, but Java {MIN_JAVA}+ is required"
    return home, major


def resolve_java_home() -> tuple[Path, int, str]:
    """Resolve a usable JDK, preferring JAVA_HOME and then PATH."""
    configured = os.environ.get("JAVA_HOME", "").strip()
    if configured:
        result = inspect_home(Path(configured))
        if not isinstance(result, str):
            return result[0], result[1], "JAVA_HOME"
        print(f"warning: JAVA_HOME is not usable: {result}", file=sys.stderr)

    java = shutil.which("java")
    if java is None:
        raise RuntimeError(
            "JAVA_HOME is not set and no java executable was found on PATH.\n"
            f"Install JDK {MIN_JAVA}+ and set JAVA_HOME, for example:\n"
            "  export JAVA_HOME=/path/to/jdk-26"
        )

    derived = java_home_from_java(Path(java))
    if derived is None:
        raise RuntimeError(
            f"could not determine java.home from {java}.\n"
            f"Set JAVA_HOME to a JDK {MIN_JAVA}+ installation."
        )

    result = inspect_home(derived)
    if isinstance(result, str):
        raise RuntimeError(
            f"{result}\nSet JAVA_HOME to a JDK {MIN_JAVA}+ installation."
        )
    return result[0], result[1], f"PATH ({java})"


def maven_command() -> list[str]:
    """Return the Maven wrapper or system Maven command."""
    if os.name == "nt":
        wrapper = PROJECT_ROOT / "mvnw.cmd"
        if wrapper.is_file():
            return ["cmd", "/c", str(wrapper)]
    else:
        wrapper = PROJECT_ROOT / "mvnw"
        if wrapper.is_file():
            if os.access(wrapper, os.X_OK):
                return [str(wrapper)]
            return ["sh", str(wrapper)]

    maven = shutil.which("mvn")
    if maven:
        return [maven]

    raise RuntimeError(
        "Maven was not found. Keep the Maven wrapper (mvnw/mvnw.cmd) in the "
        "project root, or install Maven 3.9+."
    )


def main(argv: list[str] | None = None) -> int:
    """Parse arguments, validate Java, and start the launcher."""
    if sys.version_info < MIN_PYTHON:
        print(
            f"Python {MIN_PYTHON[0]}.{MIN_PYTHON[1]}+ is required, "
            f"but {sys.version.split()[0]} was found.",
            file=sys.stderr,
        )
        return 1

    parser = argparse.ArgumentParser(
        description="Check JAVA_HOME and start SimpleMinecraftLauncher."
    )
    parser.add_argument(
        "--check",
        action="store_true",
        help="only validate Java/Python and print the detected paths",
    )
    args = parser.parse_args(argv)

    try:
        home, major, source = resolve_java_home()
    except RuntimeError as error:
        print(f"error: {error}", file=sys.stderr)
        return 1

    print(f"Python: {sys.version.split()[0]}")
    print(f"JDK: {home} (Java {major}, from {source})")

    if args.check:
        print("Java check passed.")
        return 0

    try:
        command = maven_command()
    except RuntimeError as error:
        print(f"error: {error}", file=sys.stderr)
        return 1

    env = os.environ.copy()
    env["JAVA_HOME"] = str(home)
    env["PATH"] = str(home / "bin") + os.pathsep + env.get("PATH", "")

    command = command + ["-DskipTests", "compile", "javafx:run"]
    print("Starting SimpleMinecraftLauncher...")
    print("Command:", " ".join(command))

    try:
        return subprocess.run(command, cwd=PROJECT_ROOT, env=env).returncode
    except KeyboardInterrupt:
        return 130
    except OSError as error:
        print(f"error: could not start Maven: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
