package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import static com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightBean.COSMETICAL_PRICE_MODIFIER;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TeamWeightsTab extends VerticalLayout {

	private TeamWeightsGrid teamWeightsGrid;
	private Label balanceLabel;

	private List<TeamWeightBean> teamWeights;

	public TeamWeightsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(true);
		setSizeFull();
		addStyleName("teamweights");

		teamWeights = singleLeagueServiceprovider.getContestantWeights(league).stream()
				.map(contestantWeight -> new TeamWeightBean(contestantWeight))
				.sorted(Comparator.comparingInt(TeamWeightBean::getPowerIndex).reversed())
				.collect(Collectors.toList());
		teamWeightsGrid = new TeamWeightsGrid(league, DataProvider.fromStream(teamWeights.stream()));
		teamWeightsGrid.setWidth(70, Unit.PERCENTAGE);

		initRightSide(league, singleLeagueServiceprovider);
		addComponent(teamWeightsGrid);
	}

	public void initRightSide(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		balanceLabel = new Label(getWeightToDistributeString(), ContentMode.HTML);
		balanceLabel.addStyleName("balance-label");

		teamWeightsGrid.weightChanged()
				.subscribe(beanChanged -> {
			boolean exceedsLimit = getWeightToDistribute() < 0;
			boolean isInTime = LocalDateTime.now().isBefore(league.getLeague_starting_date());
			balanceLabel.setValue(getWeightToDistributeString());
			if (!isInTime) {
				Notification.show(Resources.getMessage("cannotPurchaseStock", DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date())), Notification.Type.WARNING_MESSAGE);
			} else if (exceedsLimit) {
				Notification.show(Resources.getMessage("cannotExceedBalanceLimit"), Notification.Type.WARNING_MESSAGE);
				balanceLabel.addStyleName("red");
			} else if (beanChanged.isValid()) {
				singleLeagueServiceprovider.getContestantWeightRepository().saveAndFlush(beanChanged.getBean().setWeightAndContestantWeight());
				balanceLabel.removeStyleName("red");
			}
		});

		Label infoLabel = new Label("Changes can be made until " + DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date()));
		infoLabel.addStyleName(ValoTheme.LABEL_LIGHT);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);

		addComponent(balanceLabel);
		addComponent(infoLabel);
	}

	public String getWeightToDistributeString() {
		return VaadinIcons.WALLET.getHtml() + "   $" + DecimalUtil.getTwoDecimalsThousandSeperator(getWeightToDistribute());
	}

	public Double getWeightToDistribute() {
		Double sumOfDistributedWeight = teamWeights.stream()
				.mapToDouble(TeamWeightBean::getPricePayed)
				.sum();
		return (100d - sumOfDistributedWeight) * COSMETICAL_PRICE_MODIFIER;
	}

}
