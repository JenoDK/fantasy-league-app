package com.jeno.fantasyleague.backend.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "league_users")
@AssociationOverrides({
		@AssociationOverride(name = "pk.user",
				joinColumns = @JoinColumn(name = "user_id")),
		@AssociationOverride(name = "pk.league",
				joinColumns = @JoinColumn(name = "league_id")) })
public class LeagueUser implements java.io.Serializable {

	public enum HelpStage {
		BUY_STOCKS(1);

		private final int stage;

		HelpStage(int stage) {
			this.stage = stage;
		}

		public int getStage() {
			return stage;
		}
	}

	private LeagueUserId pk = new LeagueUserId();

	private boolean show_help;

	@Enumerated(EnumType.STRING)
	private HelpStage help_stage;

	@EmbeddedId
	public LeagueUserId getPk() {
		return pk;
	}

	public void setPk(LeagueUserId pk) {
		this.pk = pk;
	}

	@Transient
	public User getUser() {
		return getPk().getUser();
	}

	public void setUser(User user) {
		getPk().setUser(user);
	}

	@Transient
	public League getLeague() {
		return getPk().getLeague();
	}

	public void setLeague(League league) {
		getPk().setLeague(league);
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
}
