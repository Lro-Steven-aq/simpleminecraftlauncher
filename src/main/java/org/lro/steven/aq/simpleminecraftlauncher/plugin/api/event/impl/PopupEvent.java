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
package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

public class PopupEvent implements Event {
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

    public String getMessage() {
        return message;
    }

    public String getHead() {
        return head;
    }

    public Runnable getCallableOnYes() {
        return callableOnYes;
    }

    public Runnable getCallableOnNo() {
        return callableOnNo;
    }

    public Runnable getCallableOnClose() {
        return callableOnClose;
    }

}
