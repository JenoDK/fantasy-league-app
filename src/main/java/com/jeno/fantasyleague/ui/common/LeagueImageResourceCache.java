package com.jeno.fantasyleague.ui.common;

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jeno.fantasyleague.backend.model.League;
import com.vaadin.flow.server.StreamResource;

public class LeagueImageResourceCache {

	public static final LeagueImageResourceCache INSTANCE = new LeagueImageResourceCache();

	private Map<Long, StreamResource> resourceCache = Maps.newHashMap();

	public static StreamResource addOrGetLeagueImageResource(League league) {
		if (INSTANCE.resourceCache.containsKey(league.getId())) {
			return INSTANCE.resourceCache.get(league.getId());
		} else {
			StreamResource resource = new StreamResource(
					"league_banner.png",
					() -> new ByteArrayInputStream(league.getLeague_picture()));
			INSTANCE.resourceCache.put(league.getId(), resource);
			return resource;
		}
	}

	public static void remove(League league) {
		INSTANCE.resourceCache.remove(league.getId());
	}
}
