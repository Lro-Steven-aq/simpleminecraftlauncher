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
package org.lro.steven.aq.simpleminecraftlauncher.metadata.game.version;

import org.lro.steven.aq.simpleminecraftlauncher.json.configuaration.Features;
import org.lro.steven.aq.simpleminecraftlauncher.metadata.game.OperatingSystem;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 版本 JSON 里 rules 的判定逻辑。
 * <br/>
 * 参数和依赖库都用它来判断当前机器是否适用。
 */
public final class RuleEvaluator {

    /**
     * 创建 {@code RuleEvaluator} 实例。
     */
    private RuleEvaluator() {
    }

    /**
     * 按照规则列表判断是否允许。
     * 后面的规则覆盖前面的规则，默认不允许。
     */
    public static boolean isAllowed(List<Rule> rules, Features features) {
        boolean allowed = false;
        if (rules == null || rules.isEmpty()) {
            return true;
        }
        if (rules.stream().anyMatch(Objects::isNull)) {
            return false;
        }
        for (Rule rule : rules) {
            if (matches(rule, features)) {
                allowed = rule.action() == Action.ALLOW;
            }
        }
        return allowed;
    }

    /**
     * 内部方法：matches。
     * @param rule 参数
     * @param features 参数
     * @return 返回值
     */
    private static boolean matches(Rule rule, Features features) {
        return matchesOs(rule.os()) && matchesFeatures(rule.features(), features);
    }

    /**
     * 内部方法：matches Os。
     * @param os 参数
     * @return 返回值
     */
    private static boolean matchesOs(Rule.Os os) {
        if (os == null) {
            return true;
        }
        if (os.name() == null || os.name() != OperatingSystem.getOs()) {
            return false;
        }
        return os.versionRange() == null || checkVersionRange(os.versionRange());
    }

    /**
     * 内部方法：matches Features。
     * @param required 参数
     * @param features 参数
     * @return 返回值
     */
    private static boolean matchesFeatures(Map<String, Boolean> required, Features features) {
        if (required == null) {
            return true;
        }
        if (features == null) {
            return false;
        }
        for (Map.Entry<String, Boolean> entry : required.entrySet()) {
            if (!features.getOrDefault(entry.getKey(), false).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前系统版本是否在给定范围内（仅 Windows 使用版本范围）。
     */
    private static boolean checkVersionRange(VersionRange versionRange) {
        if (versionRange == null) {
            return true;
        }
        if (OperatingSystem.getOs() != OperatingSystem.WINDOWS) {
            return true;
        }
        String[] currentVersion = OperatingSystem.getVersion().split("\\.");
        if (versionRange.max() != null && isGreaterThan(currentVersion, versionRange.max().split("\\."))) {
            return false;
        }
        return versionRange.min() == null || isGreaterThan(currentVersion, versionRange.min().split("\\."));
    }

    /**
     * 判断是否 Greater Than。
     * @param a 参数
     * @param b 参数
     * @return 返回值
     */
    private static boolean isGreaterThan(String[] a, String[] b) {
        int length = Math.max(a.length, b.length);
        for (int i = 0; i < length; i++) {
            String segmentA = i < a.length ? a[i] : "0";
            String segmentB = i < b.length ? b[i] : "0";
            int diff = Integer.compare(stringToInteger(segmentA), stringToInteger(segmentB));
            if (diff < 0) {
                return false;
            } else if (diff > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 内部方法：string To Integer。
     * @param string 参数
     * @return 返回值
     */
    private static int stringToInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
