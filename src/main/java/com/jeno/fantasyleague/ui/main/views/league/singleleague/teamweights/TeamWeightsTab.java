package com.jeno.fantasyleague.ui.main.views.league.singleleague.teamweights;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.RxUtil;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import io.reactivex.functions.Predicate;

public class TeamWeightsTab extends HorizontalLayout {

	private TeamWeightsGrid teamWeightsGrid;
	private Label weightLeftField;
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
		rightSide.setMargin(false);
		rightSide.setSpacing(true);

		resetToOriginal = new CustomButton("Reset to original", VaadinIcons.REFRESH);
		RxUtil.clicks(resetToOriginal).subscribe(ignored -> teamWeights.stream().forEach(teamWeightBean -> {
			teamWeightBean.reset();
			weightLeftField.setValue(getWeightToDistributeString());
		}));
		rightSide.addComponent(resetToOriginal);
		rightSide.addComponents(new Label());

		weightLeftField = new Label(getWeightToDistributeString());
		rightSide.addComponent(weightLeftField);
		teamWeightsGrid.weightChanged().subscribe(ignored ->
			weightLeftField.setValue(getWeightToDistributeString()));

		submit = new CustomButton("Update weights", VaadinIcons.USER_CHECK);
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
					if (LocalDateTime.now().isBefore(league.getLeague_starting_date())) {
						singleLeagueServiceprovider.getContestantWeightRepository().saveAll(getConestantWeights());
						errorLabel.setStyleName(ValoTheme.LABEL_SUCCESS);
						errorLabel.setValue("Changes saved successfully");
						errorLabel.setVisible(true);
					} else {
						setError("Changes can not be done after " + DateUtil.DATE_TIME_FORMATTER.format(league.getLeague_starting_date()));
					}
				});
	}

	public String getWeightToDistributeString() {
		return "Weight to distribute: " + getWeightToDistribute();
	}

	public Predicate<Button.ClickEvent> distributedCorrectly() {
		return ignored -> {
			boolean allDistributed = getWeightToDistribute() == 0;
			if (!allDistributed) {
				setError("Please distribute 100 points");
			} else {
				errorLabel.setVisible(false);
			}
			return allDistributed;
		};
	}

	public void setError(String msg) {
		errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
		errorLabel.setValue(msg);
		errorLabel.setVisible(true);
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
