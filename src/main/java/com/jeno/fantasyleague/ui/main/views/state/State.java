package com.jeno.fantasyleague.ui.main.views.state;

import com.jeno.fantasyleague.ui.main.views.admin.AdminModule;
import com.jeno.fantasyleague.ui.main.views.league.LeagueModule;
import com.jeno.fantasyleague.ui.main.views.profile.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum State {

	LEAGUE(LeagueModule.class, "League", VaadinIcon.HOME, 0),
	PROFILE(ProfileView.class, "Profile", VaadinIcon.USER, 20),
	ADMIN(AdminModule.class, "Admin", VaadinIcon.SWORD, 30);

	private Class<? extends Component> viewClass;
	private String name;
	private VaadinIcon icon;
	private int seq;

	State(Class<? extends Component> viewClass, String name, VaadinIcon icon, int seq) {
		this.viewClass = viewClass;
		this.name = name;
		this.icon = icon;
		this.seq = seq;
	}

	public static List<State> getMenuItems() {
		return Arrays.stream(State.values())
				.collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public Class<? extends Component> getViewClass() {
		return viewClass;
	}

	public VaadinIcon getIcon() {
		return icon;
	}

	public int getSeq() {
		return seq;
	}

	public static class StateUrlConstants {

		public static final String ROOT = "";
		public static final String LEAGUE = "league";
		public static final String PROFILE = "profile";
		public static final String ADMIN = "admin";
		public static final String PAGE_NOT_FOUND = "notFound";

		private StateUrlConstants() {
		}
	}

}
