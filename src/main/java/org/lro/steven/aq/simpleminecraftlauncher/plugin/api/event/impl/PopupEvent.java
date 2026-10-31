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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

/** 弹窗事件，携带标题、文本和按钮回调。 */
public class PopupEvent implements Event {
    /**
     * 创建一条弹窗事件。
     * @param title 参数
     * @param head 参数
     * @param msg 参数
     * @param callableOnYes 参数
     * @param callableOnNo 参数
     * @param callableOnClose 参数
     * @return 返回值
     */
    public static Event of(String title,
                           String head,
                           String msg,
                           Runnable callableOnYes,
                           Runnable callableOnNo,
                           Runnable callableOnClose) {
        return new PopupEvent(title,head, msg, callableOnYes, callableOnNo, callableOnClose);
    }

    private final String title;
    private final String head;
    private final String message;
    private final Runnable callableOnYes;
    private final Runnable callableOnNo;
    private final Runnable callableOnClose;

    private PopupEvent(String title,
                       String head,
                       String msg,
                       Runnable callableOnYes,
                       Runnable callableOnNo,
                       Runnable callableOnClose) {
        this.title = title;
        this.head = head;
        this.message = msg;
        this.callableOnYes = callableOnYes;
        this.callableOnNo = callableOnNo;
        this.callableOnClose = callableOnClose;
    }

    @Override
    public String source() {
        return "popup event";
    }

    public String getTitle() {
        return title;
    }

    /**
     * 返回弹窗正文。
     * @return 返回值
     */
    public String getMessage() {
        return message;
    }

    /**
     * 返回弹窗头部文本。
     * @return 返回值
     */
    public String getHead() {
        return head;
    }

    /**
     * 返回“是”按钮的回调。
     * @return 返回值
     */
    public Runnable getCallableOnYes() {
        return callableOnYes;
    }

    /**
     * 返回“否”按钮的回调。
     * @return 返回值
     */
    public Runnable getCallableOnNo() {
        return callableOnNo;
    }

    /**
     * 返回关闭按钮的回调。
     * @return 返回值
     */
    public Runnable getCallableOnClose() {
        return callableOnClose;
    }

}
