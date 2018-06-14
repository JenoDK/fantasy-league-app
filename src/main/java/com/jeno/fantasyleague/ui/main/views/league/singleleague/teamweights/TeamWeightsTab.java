package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import static com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights.TeamWeightBean.COSMETICAL_PRICE_MODIFIER;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeno.fantasyleague.model.ContestantWeight;
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
		addStyleName("teamweights");

		fetchAndSetTeamWeights();
		teamWeightsGrid = new TeamWeightsGrid(league, DataProvider.fromStream(teamWeights.stream()));
		teamWeightsGrid.setWidth(70, Unit.PERCENTAGE);

		initRightSide(league, singleLeagueServiceprovider);
		addComponent(teamWeightsGrid);
	}

	public void fetchAndSetTeamWeights() {
		teamWeights = singleLeagueServiceprovider.getContestantWeights(league).stream()
				.map(contestantWeight -> new TeamWeightBean(contestantWeight))
				.sorted(Comparator.comparingInt(TeamWeightBean::getPowerIndex).reversed())
				.collect(Collectors.toList());
	}

	public void initRightSide(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		balanceLabel = new Label(getWeightToDistributeString(getWeightToDistribute()), ContentMode.HTML);
		balanceLabel.addStyleName("balance-label");

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
						Notification.show("Server-side changes were detected, refreshing and then you can try again", Notification.Type.WARNING_MESSAGE);
						fetchAndSetTeamWeights();
						teamWeightsGrid.setItems(teamWeights);
						balanceLabel.setValue(getWeightToDistributeString(getWeightToDistribute(stream)));
					} else {
						boolean exceedsLimit = clientSideWeightToDistribute.compareTo(BigDecimal.ZERO) == -1;
						boolean isInTime = LocalDateTime.now().isBefore(league.getLeague_starting_date());
						balanceLabel.setValue(getWeightToDistributeString(clientSideWeightToDistribute));
						if (!isInTime) {
							Notification.show(Resources.getMessage("cannotPurchaseStock", DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date())), Notification.Type.WARNING_MESSAGE);
						} else if (exceedsLimit) {
							Notification.show(Resources.getMessage("cannotExceedBalanceLimit"), Notification.Type.WARNING_MESSAGE);
							balanceLabel.addStyleName("red");
						} else if (beanChanged.isValid()) {
							singleLeagueServiceprovider.getContestantWeightRepository().saveAndFlush(beanChanged.getBean().setWeightAndContestantWeight());
							balanceLabel.removeStyleName("red");
						}
					}
		});

		Label infoLabel = new Label("Changes can be made until " + DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date()));
		infoLabel.addStyleName(ValoTheme.LABEL_LIGHT);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);

		addComponent(balanceLabel);
		addComponent(infoLabel);
	}

	public String getWeightToDistributeString(BigDecimal weightToDistribute) {
		return VaadinIcons.WALLET.getHtml() + "   $" + DecimalUtil.getTwoDecimalsThousandSeperator(weightToDistribute);
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
