package com.jeno.fantasyleague.ui.main.views.state;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.icon.VaadinIcon;

public enum State {

    LEAGUE(StateUrlConstants.LEAGUE, "League", VaadinIcon.HOME, 0),
    PROFILE(StateUrlConstants.PROFILE, "Profile", VaadinIcon.USER, 20);

    private String identifier;
    private String name;
    private VaadinIcon icon;
    private int seq;

    State(String identifier, String name, VaadinIcon icon, int seq) {
        this.identifier = identifier;
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

    public String getIdentifier() {
        return identifier;
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

        private StateUrlConstants() {
        }
    }

}
