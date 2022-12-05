package com.jeno.fantasyleague.ui.main.broadcast;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.common.collect.ArrayListMultimap;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.chat.ChatMessage;
import com.vaadin.flow.shared.Registration;

public class ChatBroadcaster implements Serializable {

	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	private static ArrayListMultimap<Long, Consumer<ChatMessage>> listeners = ArrayListMultimap.create();

	public static synchronized Registration register(
			Long userId,
			Consumer<ChatMessage> listener) {
		listeners.put(userId, listener);
		return () -> {
			synchronized (ChatBroadcaster.class) {
				listeners.remove(userId, listener);
			}
		};
	}

	public static synchronized void broadcastChatMessage(final Long leagueId, final ChatMessage message) {
		for (Consumer<ChatMessage> listener : listeners.get(leagueId)) {
			executorService.execute(() -> listener.accept(message));
		}
	}
}
