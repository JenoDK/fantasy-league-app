package com.jeno.fantasyleague.backend.data.service.leaguetemplates.worldcup2018;

import com.jeno.fantasyleague.backend.model.LeagueSetting;

import java.util.Objects;

public class IntegerSettingsBean {

	private final LeagueSetting leagueSetting;

	private Integer value;

	public IntegerSettingsBean(LeagueSetting leagueSetting) {
		this.leagueSetting = leagueSetting;
		this.value = Integer.valueOf(leagueSetting.getValue());
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return leagueSetting.getName();
	}

	public LeagueSetting getModelItem() {
		leagueSetting.setValue(value.toString());
		return leagueSetting;
	}

	public boolean valueChanged() {
		return !Objects.equals(value, Integer.valueOf(leagueSetting.getValue()));
	}

}
