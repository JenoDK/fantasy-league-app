package com.jeno.fantasyleague.ui.main.views.state;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.util.Images;

public enum State {

//    HOME(StateUrlConstants.HOME, "Home", true, Images.Icons.HOME_ICON, 0),
    LEAGUE(StateUrlConstants.LEAGUE, "League", true, Images.Icons.LEAGUE_ICON, 0),
    PROFILE(StateUrlConstants.PROFILE, "Profile", false, 20);

    private String identifier;
    private String name;
    private boolean showInMenuBar;
    private String icon = "";
    private int seq;

    State(String identifier, String name, boolean showInMenuBar, int seq) {
        this.identifier = identifier;
        this.name = name;
        this.showInMenuBar = showInMenuBar;
        this.seq = seq;
    }

    State(String identifier, String name, boolean showInMenuBar, String icon, int seq) {
        this.identifier = identifier;
        this.name = name;
        this.showInMenuBar = showInMenuBar;
        this.icon = icon;
        this.seq = seq;
    }

    public static List<State> getMenuItems() {
        return Arrays.stream(State.values())
                .filter(State::getShowInMenuBar)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean getShowInMenuBar() {
        return showInMenuBar;
    }

    public String getIcon() {
        return icon;
    }

    public int getSeq() {
        return seq;
    }

    public static class StateUrlConstants {

        public static final String LEAGUE = "league";
        public static final String PROFILE = "profile";

        private StateUrlConstants() {
        }
    }

}
