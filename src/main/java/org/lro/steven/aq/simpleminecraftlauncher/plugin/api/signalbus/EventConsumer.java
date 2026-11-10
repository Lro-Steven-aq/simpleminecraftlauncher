package org.lro.steven.aq.simpleminecraftlauncher.plugin.api.signalbus;

import org.lro.steven.aq.simpleminecraftlauncher.plugin.api.event.Event;

import java.util.function.Consumer;

/**
 * 表示 {@code EventConsumer}。
 */
public interface EventConsumer extends Consumer<Event> {
}
