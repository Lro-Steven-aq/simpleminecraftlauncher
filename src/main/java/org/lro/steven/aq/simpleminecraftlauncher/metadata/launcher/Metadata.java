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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher;

import javafx.scene.image.Image;
import org.glavo.url.WebURL;
import org.lro.steven.aq.simpleminecraftlauncher.ApplicationConfigurationLoader;
import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.launcher.conf.ApplicationConfiguration;

import java.io.InputStream;

/** 启动器运行期元数据。 */
public final class Metadata {

    private final OperatingSystem system;
    private final String systemVersion;
    private final Features features;
    private final Image preciousBackgroundImage;
    private final ApplicationConfiguration configurationMapper;

    /**
     * 创建 {@code Metadata} 实例。
     * @param system 参数
     * @param systemVersion 参数
     * @param features 参数
     * @param preciousBackgroundImage 参数
     * @param configurationMapper 参数
     */
    private Metadata(OperatingSystem system, String systemVersion, Features features, Image preciousBackgroundImage, ApplicationConfiguration configurationMapper) {
        this.system = system;
        this.systemVersion = systemVersion;
        this.features = features;
        this.preciousBackgroundImage = preciousBackgroundImage;
        this.configurationMapper = configurationMapper;
    }

    /**
     * 根据配置构建运行期元数据。
     * @param features 参数
     * @param configurationMapper 参数
     * @return 返回值
     */
    public static Metadata from(Features features, ApplicationConfiguration configurationMapper) {
        WebURL url = configurationMapper.userSettings() == null
                ? null
                : configurationMapper.userSettings().preciousBackgroundImageUrl();
        return new Metadata(OperatingSystem.getOs(),
                OperatingSystem.getVersion(),
                features,
                loadBackground(url),
                configurationMapper);
    }

    /**
     * 加载 Background。
     * @param url 参数
     * @return 返回值
     */
    private static Image loadBackground(WebURL url) {
        if (url != null) {
            try {
                Image image = new Image(url.toString());
                if (!image.isError()) {
                    return image;
                }
            } catch (RuntimeException ignored) {
                // 继续使用默认背景。
            }
        }
        InputStream stream = Metadata.class.getResourceAsStream(
                "/org/lro/steven/aq/simpleminecraftlauncher/background.jpg");
        return stream == null ? null : new Image(stream);
    }

    /**
     * 返回当前操作系统。
     * @return 返回值
     */
    public OperatingSystem system() {
        return system;
    }

    /**
     * 返回系统版本。
     * @return 返回值
     */
    public String systemVersion() {
        return systemVersion;
    }

    /**
     * 返回 features 开关。
     * @return 返回值
     */
    public Features features() {
        return features;
    }

    /**
     * 返回背景图片。
     * @return 返回值
     */
    public Image preciousBackgroundImage() {
        return preciousBackgroundImage;
    }

    /**
     * 返回配置对象。
     * @return 返回值
     */
    public ApplicationConfiguration configurationMapper() {
        return configurationMapper;
    }

    /**
     * 把运行期配置（包括快速启动开关、Java 路径、游戏目录等）保存到默认配置文件。
     */
    public void save() {
        configurationMapper.save(ApplicationConfigurationLoader.DEFAULT_PATH);
    }
}
