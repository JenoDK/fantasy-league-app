package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model;

public class DataRole {

	private final String type;
	private final String role;

	public DataRole(String role) {
		this.type = "string";
		this.role = role;
	}

	public String getType() {
		return type;
	}

	public String getRole() {
		return role;
	}
}
