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
package org.lro.steven.aq.simpleminecraftlauncher.exception;

/** 启动器自定义异常基类。 */
public abstract class Exception extends java.lang.RuntimeException {
    private String message = null;

    /**
     * 设置异常消息。
     * @param msg 参数
     */
    public void setMessage(String msg) {
        message = msg;
    }

    @Override
    public String toString() {
        return message;
    }
}
