package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import static com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightBean.COSMETICAL_PRICE_MODIFIER;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;

public class TeamWeightsTab extends VerticalLayout {

	private League league;
	private SingleLeagueServiceProvider singleLeagueServiceprovider;
	private TeamWeightsGrid teamWeightsGrid;
	private Label balanceLabel;

	private List<TeamWeightBean> teamWeights;

	public TeamWeightsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;
		this.league = league;
		setMargin(true);
		setSizeFull();
		addClassName("teamweights");

		fetchAndSetTeamWeights();
		teamWeightsGrid = new TeamWeightsGrid(league, DataProvider.fromStream(teamWeights.stream()));
		teamWeightsGrid.setWidth("7Opx");

		initRightSide(league, singleLeagueServiceprovider);
		add(teamWeightsGrid);
	}

	public void fetchAndSetTeamWeights() {
		teamWeights = singleLeagueServiceprovider.getContestantWeights(league).stream()
				.map(contestantWeight -> new TeamWeightBean(contestantWeight))
				.sorted(Comparator.comparingInt(TeamWeightBean::getPowerIndex).reversed())
				.collect(Collectors.toList());
	}

	public void initRightSide(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		balanceLabel = new Label(getWeightToDistributeString(getWeightToDistribute()));
		balanceLabel.addClassName("balance-label");

		teamWeightsGrid.weightChanged()
				.subscribe(beanChanged -> {
					List<ContestantWeight> serversideStream = singleLeagueServiceprovider.getContestantWeights(league);
					Stream<TeamWeightBean> stream = serversideStream.stream()
							.map(contestantWeight -> new TeamWeightBean(contestantWeight));
					Stream<TeamWeightBean> streamWithBeanReplaced = serversideStream.stream()
							.map(contestantWeight -> {
								if (beanChanged.getBean().getContestantWeight().getId().equals(contestantWeight.getId())) {
									return beanChanged.getBean();
								} else {
									return new TeamWeightBean(contestantWeight);
								}
							});
					BigDecimal serversideWeightWithBeanReplacedToDistribute = getWeightToDistribute(streamWithBeanReplaced);
					BigDecimal clientSideWeightToDistribute = getWeightToDistribute();
					if (!serversideWeightWithBeanReplacedToDistribute.equals(clientSideWeightToDistribute)) {
						Notification.show("Server-side changes were detected, refreshing and then you can try again");
						fetchAndSetTeamWeights();
						teamWeightsGrid.setItems(teamWeights);
						balanceLabel.setText(getWeightToDistributeString(getWeightToDistribute(stream)));
					} else {
						boolean exceedsLimit = clientSideWeightToDistribute.compareTo(BigDecimal.ZERO) == -1;
						boolean isInTime = LocalDateTime.now().isBefore(league.getLeague_starting_date());
						balanceLabel.setText(getWeightToDistributeString(clientSideWeightToDistribute));
						if (!isInTime) {
							Notification.show(Resources.getMessage("cannotPurchaseStock", DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date())));
						} else if (exceedsLimit) {
							Notification.show(Resources.getMessage("cannotExceedBalanceLimit"));
							balanceLabel.addClassName("red");
						} else if (beanChanged.isValid()) {
							singleLeagueServiceprovider.getContestantWeightRepository().saveAndFlush(beanChanged.getBean().setWeightAndContestantWeight());
							balanceLabel.removeClassName("red");
						}
					}
		});

		Label infoLabel = new Label("Changes can be made until " + DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date()));
//		infoLabel.addClassName(ValoTheme.LABEL_LIGHT);
//		infoLabel.addClassName(ValoTheme.LABEL_TINY);

		add(balanceLabel);
		add(infoLabel);
	}

	public String getWeightToDistributeString(BigDecimal weightToDistribute) {
		return VaadinIcon.WALLET.create().getElement().getOuterHTML() + "   $" + DecimalUtil.getTwoDecimalsThousandSeperator(weightToDistribute);
	}

	public BigDecimal getWeightToDistribute() {
		return getWeightToDistribute(teamWeights.stream());
	}

	private BigDecimal getWeightToDistribute(Stream<TeamWeightBean> stream) {
		BigDecimal sumOfDistributedWeight = stream
				.map(TeamWeightBean::getPricePayed)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return BigDecimal.valueOf(100).subtract(sumOfDistributedWeight).multiply(COSMETICAL_PRICE_MODIFIER);
	}

}
