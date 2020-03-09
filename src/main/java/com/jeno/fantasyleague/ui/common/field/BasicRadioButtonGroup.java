package com.jeno.fantasyleague.ui.common.field;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class BasicRadioButtonGroup extends RadioButtonGroup<BasicRadioButtonGroup.RadioButtonItem> {

	public static class RadioButtonItem {
		private String key;
		private String value;

		public RadioButtonItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

}
