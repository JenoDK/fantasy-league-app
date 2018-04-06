package com.jeno.fantasyleague.ui.main.broadcast;

import com.google.common.collect.ArrayListMultimap;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster implements Serializable {

	static ExecutorService executorService =
			Executors.newSingleThreadExecutor();

	private static ArrayListMultimap<Long, BroadcastListener> listeners = ArrayListMultimap.create();

	public static synchronized void register(
			Long userId,
			BroadcastListener listener) {
		listeners.put(userId, listener);
	}

	public static synchronized void unregister(Long userId, BroadcastListener listener) {
		listeners.remove(userId, listener);
	}

	public static synchronized void broadcast(final Long userId, final Notification notification) {
		listeners.get(userId).forEach(listener -> listener.receiveBroadcast(notification));
	}

	public interface BroadcastListener {
		void receiveBroadcast(Notification notification);
	}
}
