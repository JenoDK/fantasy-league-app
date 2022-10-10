package com.jeno.fantasyleague.backend.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "league_users")
public class LeagueUser implements java.io.Serializable {

	public enum HelpStage {
		INTRO(0, "introTip", "introTitle"),
		BUY_STOCKS(1, "buyStocksTip", "buyStocksTitle"),
		FILL_PREDICTIONS(2, "fillPredictionsTip", "fillPredictionsTitle"),
		CHECK_OVERVIEW(3, "checkOverviewTip", "checkOverviewTitle");

		private final int stage;
		private final String resourceKey;
		private final String titleResourceKey;

		HelpStage(int stage, String resourceKey, String titleResourceKey) {
			this.stage = stage;
			this.resourceKey = resourceKey;
			this.titleResourceKey = titleResourceKey;
		}

		public boolean isFirstStage() {
			OptionalInt minStage = Arrays.stream(HelpStage.values())
					.mapToInt(HelpStage::getStage)
					.min();
			if (minStage.isPresent()) {
				return minStage.getAsInt() == getStage();
			}
			return false;
		}

		public boolean isLastStage() {
			OptionalInt maxStage = Arrays.stream(HelpStage.values())
					.mapToInt(HelpStage::getStage)
					.max();
			if (maxStage.isPresent()) {
				return maxStage.getAsInt() == getStage();
			}
			return false;
		}

		public String getResourceKey() {
			return resourceKey;
		}

		public String getTitleResourceKey() {
			return titleResourceKey;
		}

		public Optional<HelpStage> getPreviousStage() {
			int currentStage = getStage();
			return Arrays.stream(HelpStage.values())
					.filter(stage -> stage.getStage() == (currentStage - 1))
					.findFirst();
		}

		public Optional<HelpStage> getNextStage() {
			int currentStage = getStage();
			return Arrays.stream(HelpStage.values())
					.filter(stage -> stage.getStage() == (currentStage + 1))
					.findFirst();
		}

		public int getStage() {
			return stage;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean show_help;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "league_id")
	private League league;

	@Enumerated
	private HelpStage help_stage;

	private int unread_messages;

	public LeagueUser() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public boolean isShow_help() {
		return show_help;
	}

	public void setShow_help(boolean show_help) {
		this.show_help = show_help;
	}

	public HelpStage getHelp_stage() {
		return help_stage;
	}

	public void setHelp_stage(HelpStage help_stage) {
		this.help_stage = help_stage;
	}

	public int getUnread_messages() {
		return unread_messages;
	}

	public void setUnread_messages(int unread_messages) {
		this.unread_messages = unread_messages;
	}
}
