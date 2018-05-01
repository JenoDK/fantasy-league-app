package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.functions.Predicate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamWeightsTab extends HorizontalLayout {

	private TeamWeightsGrid teamWeightsGrid;
	private TextField weightLeftField;
	private Button resetToOriginal;
	private Button submit;
	private Label errorLabel;

	private List<TeamWeightBean> teamWeights;

	public TeamWeightsTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		super();
		setMargin(true);
		setSizeFull();

		teamWeights = singleLeagueServiceprovider.getContestantWeights(league).stream()
				.map(contestantWeight -> new TeamWeightBean(contestantWeight))
				.sorted(Comparator.comparingInt(TeamWeightBean::getPowerIndex).reversed())
				.collect(Collectors.toList());
		teamWeightsGrid = new TeamWeightsGrid(league, DataProvider.fromStream(teamWeights.stream()));
		addComponent(teamWeightsGrid);

		if (LocalDateTime.now().isBefore(league.getLeague_starting_date())) {
			initRightSide(league, singleLeagueServiceprovider);
		}
	}

	public void initRightSide(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		VerticalLayout rightSide = new VerticalLayout();

		resetToOriginal = new Button("Reset to original", VaadinIcons.REFRESH);
		RxUtil.clicks(resetToOriginal).subscribe(ignored -> teamWeights.stream().forEach(teamWeightBean -> {
			teamWeightBean.reset();
			weightLeftField.setValue("" + getWeightToDistribute());
		}));
		rightSide.addComponent(resetToOriginal);
		rightSide.addComponents(new Label());

		weightLeftField = new TextField("Weight to distribute");
		weightLeftField.setReadOnly(true);
		weightLeftField.setValue("" + getWeightToDistribute());
		rightSide.addComponent(weightLeftField);
		teamWeightsGrid.weightChanged().subscribe(ignored ->
			weightLeftField.setValue("" + getWeightToDistribute()));

		submit = new Button("Submit", VaadinIcons.USER_CHECK);
		rightSide.addComponent(submit);

		Label infoLabel = new Label("Changes can be made until " + DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date()));
		infoLabel.addStyleName(ValoTheme.LABEL_LIGHT);
		infoLabel.addStyleName(ValoTheme.LABEL_TINY);
		rightSide.addComponent(infoLabel);

		errorLabel = new Label();
		errorLabel.setVisible(false);
		rightSide.addComponents(errorLabel);
		addComponent(rightSide);

		RxUtil.clicks(submit)
				.filter(distributedCorrectly())
				.subscribe(ignored -> {
					singleLeagueServiceprovider.getContestantWeightRepository().saveAll(getConestantWeights());
					errorLabel.setStyleName(ValoTheme.LABEL_SUCCESS);
					errorLabel.setValue("Changes saved successfully");
					errorLabel.setVisible(true);
				});
	}

	public Predicate<Button.ClickEvent> distributedCorrectly() {
		return ignored -> {
			boolean allDistributed = getWeightToDistribute() == 0;
			if (!allDistributed) {
				errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
				errorLabel.setValue("Please distribute all available points");
				errorLabel.setVisible(true);
			} else {
				errorLabel.setVisible(false);
			}
			return allDistributed;
		};
	}

	public Integer getWeightToDistribute() {
		int sumOfDistributedWeight = teamWeights.stream()
				.mapToInt(TeamWeightBean::getWeight)
				.sum();
		return 100 - sumOfDistributedWeight;
	}

	public List<ContestantWeight> getConestantWeights() {
		return teamWeights.stream()
				.map(TeamWeightBean::setWeightAndContestantWeight)
				.collect(Collectors.toList());
	}
}
