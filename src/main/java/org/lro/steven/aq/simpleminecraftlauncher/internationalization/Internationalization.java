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
 *
 * Plugin Exception:
 * Plugins that use the package org.lro.steven.aq.simpleminecraftlauncher.plugin.api
 * and its subpackages, together with the JAR files they call, are not required
 * to be licensed under the GPLv3 and may be distributed under any license
 * terms. See the LICENSE file ("SimpleMinecraftLauncher Plugin Exception")
 * for the full definition and conditions.
 */
package org.lro.steven.aq.simpleminecraftlauncher.internationalization;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.PluginManager;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl.LogEvent;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to process i18n
 * @author lro.steven.aq
 * @since 0.1
 */
public final class Internationalization {

    private static String INTERNATIONALIZATION_FILE_ROOT = ".simpleminecraftlauncher/i18n";
    private final Map<Locale, Properties> properties = new HashMap<>();    // K -> V
    private final static Internationalization internationalization;

    static {
        internationalization = new Internationalization();
        initialize();
    }
    private static void initialize() {
        try {
            internationalization.addProperties(
                    Locale.ENGLISH,
                    internationalization.load("%s/language.properties".formatted(INTERNATIONALIZATION_FILE_ROOT))
            );
            try (var stream = Files.list(new File("%s/".formatted(INTERNATIONALIZATION_FILE_ROOT)).toPath())) {
                stream.filter((f) -> f.toFile().getName().startsWith("language_"))
                        .forEach((f) -> {
                                internationalization.addProperties(Locale.forLanguageTag(parseLanguageTag(f.toFile().getName())), buildPropertiesFromFile(f.toFile()));
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按当前语言获取国际化文本，找不到时回退到英语。
     * @param key 参数
     * @return 返回值
     */
    public static String get(String key) {
        return internationalization.properties.getOrDefault(
                Locale.getDefault(),
                internationalization.properties.get(Locale.ENGLISH)  // 退化为英语
        ).getProperty(key, key);
    }

    /**
     * 设置当前语言。
     * @param locale 参数
     */
    public void setLocale(Locale locale) {
        Locale.setDefault(locale);
    }
    private Internationalization() {}


    private void addProperties(Locale locale, Properties properties) {
        this.properties.put(locale, properties);
    }
    private Properties load(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));
        return properties;
    }
    private static String parseLanguageTag(String fileName) {
        Pattern pattern = Pattern.compile("language_(.+)");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches() ? matcher.group(1) : fileName;
    }
    private static Properties buildPropertiesFromFile(File file) {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            PluginManager.get().getSignalBus().emit("log.error", LogEvent.of("Error to read/load the language file"));
        }
        return properties;
    }
}
