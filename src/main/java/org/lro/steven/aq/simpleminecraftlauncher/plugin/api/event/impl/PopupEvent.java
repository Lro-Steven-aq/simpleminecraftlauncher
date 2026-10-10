package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.impl;

import javafx.scene.control.Alert;
import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

public class PopupEvent implements Event {
    public static Event of(String title,String head, String msg) {
        return new PopupEvent(title,head, msg);
    }

    private final String title;
    private final String head;
    private final String message;

    private PopupEvent(String title,String head, String msg) {
        this.title = title;
        this.head = head;
        this.message = msg;
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
}
