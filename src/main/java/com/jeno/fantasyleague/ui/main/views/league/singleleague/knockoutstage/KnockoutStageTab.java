package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.field.CustomButton;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class KnockoutStageTab extends VerticalLayout {

	private final static int ROW_START = 1;

	private final SingleLeagueServiceProvider singleLeagueServiceprovider;
	private final League league;

	private GridLayout bracketLayout;

	public KnockoutStageTab(League league, SingleLeagueServiceProvider singleLeagueServiceprovider) {
		this.league = league;
		this.singleLeagueServiceprovider = singleLeagueServiceprovider;

		setSpacing(false);
		setMargin(false);
		addStyleName("margin-top-10");

		bracketLayout = new GridLayout(7, ROW_START + 16);
		bracketLayout.addStyleName("bracket-gridlayout");

		fillInBracket(league);


		Panel gridLayoutPanel = new Panel();
		gridLayoutPanel.setContent(bracketLayout);
		gridLayoutPanel.addStyleName("no-visual-panel");

		Button refreshButton = new Button(VaadinIcons.REFRESH);
		refreshButton.addStyleName(ValoTheme.BUTTON_TINY);
		refreshButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		refreshButton.addClickListener(ignored -> {
			bracketLayout.removeAllComponents();
			fillInBracket(league);
		});

		addComponent(refreshButton);
		addComponent(gridLayoutPanel);
	}

	public void fillInBracket(League league) {
		addHeader(Resources.getMessage("roundOf16"), 0);
		addHeader(Resources.getMessage("quarterFinals"), 2);
		addHeader(Resources.getMessage("semiFinals"), 4);
		addHeader(Resources.getMessage("finals"), 6);

		ArrayListMultimap<Long, KnockoutGameBean> gamesPerNextId = ArrayListMultimap.create();
		List<KnockoutGameBean> eighthFinalsGames =
				fetchGamesByStage(league, FifaWorldCup2018Stages.EIGHTH_FINALS.toString());
		eighthFinalsGames.forEach(gameBean -> gamesPerNextId.put(gameBean.getGame().getNext_game_fk(), gameBean));

		Map<Long, Integer> quarterGameIdIndex = Maps.newHashMap();
		eighthFinalsGames.clear();
		int index = 0;
		for (Map.Entry<Long, Collection<KnockoutGameBean>> entry : gamesPerNextId.asMap().entrySet()) {
			quarterGameIdIndex.put(entry.getKey(), index);
			entry.getValue().forEach(eighthFinalsGames::add);
			index++;
		}
		List<KnockoutGameBean> quarterFinalsGames =
				fetchGamesByStage(league, FifaWorldCup2018Stages.QUARTER_FINALS.toString()).stream()
					.sorted(Comparator.comparingInt(gameBean -> quarterGameIdIndex.get(gameBean.getGame().getId())))
					.collect(Collectors.toList());
		List<KnockoutGameBean> semiFinalsGames =
				fetchGamesByStage(league, FifaWorldCup2018Stages.SEMI_FINALS.toString());
		List<KnockoutGameBean> finalsGames =
				fetchGamesByStage(league, FifaWorldCup2018Stages.FINALS.toString());

		addRowsToColumn(eighthFinalsGames, 0, 2, 0);
		addEighthFinalsLines();
		addRowsToColumn(quarterFinalsGames, 2, 4, 1);
		addQuarterFinalsLines();
		addRowsToColumn(semiFinalsGames, 4, 8, 3);
		addSemiFinalsLines();

		addFinals(finalsGames);
	}

	private void addHeader(String title, int column) {
		Label headerLabel = new Label(title);
		headerLabel.setWidth(100f, Unit.PERCENTAGE);
		headerLabel.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
		headerLabel.addStyleName(ValoTheme.LABEL_H3);
		headerLabel.addStyleName(ValoTheme.LABEL_COLORED);
		bracketLayout.addComponent(headerLabel, column, 0);
	}

	private void addSemiFinalsLines() {
		addTopRightLine(5, 3);
		addRightLine(5, 4);
		addRightLine(5, 5);
		addRightLine(5, 6);
		addRightLine(5, 8);
		addRightLine(5, 9);
		addRightLine(5, 10);
		addBottomRightLIne(5, 11);
	}

	private void addQuarterFinalsLines() {
		addTopRightLine(3, 1);
		addRightLine(3, 2);
		addRightLine(3, 4);
		addBottomRightLIne(3, 5);

		addTopRightLine(3, 9);
		addRightLine(3, 10);
		addRightLine(3, 12);
		addBottomRightLIne(3, 13);
	}

	public void addEighthFinalsLines() {
		addTopRightLine(1, 0);
		addBottomRightLIne(1, 2);

		addTopRightLine(1, 4);
		addBottomRightLIne(1, 6);

		addTopRightLine(1, 8);
		addBottomRightLIne(1, 10);

		addTopRightLine(1, 12);
		addBottomRightLIne(1, 14);
	}

	private void addRightLine(int column, int row) {
		bracketLayout.addComponent(new BracketColumnPlaceholder("right", false), column, ROW_START + row);
	}

	private void addBottomRightLIne(int column, int row) {
		bracketLayout.addComponent(new BracketColumnPlaceholder("bottom-right", true), column, ROW_START + row);
	}

	private void addTopRightLine(int column, int row) {
		BracketColumnPlaceholder placeholder = new BracketColumnPlaceholder("top-right", true);
		bracketLayout.addComponent(placeholder, column, ROW_START + row);
		bracketLayout.setComponentAlignment(placeholder, Alignment.BOTTOM_CENTER);
	}

	public List<KnockoutGameBean> fetchGamesByStage(League league, String stage) {
		List<Game> games = singleLeagueServiceprovider.getGameRepository().findByLeagueAndStage(league, stage);
		Map<Long, Prediction> predictionToGameIdMap = singleLeagueServiceprovider.getLoggedInUserPredictions(games).stream()
				.collect(Collectors.toMap(prediction -> prediction.getGame_fk(), Function.identity()));
		return games.stream()
				.map(game -> {
					Contestant homeTeam = game.getHome_team_fk() != null ?
							singleLeagueServiceprovider.getContestantRepository().findById(game.getHome_team_fk()).get() : null;
					Contestant awayTeam = game.getAway_team_fk() != null ?
							singleLeagueServiceprovider.getContestantRepository().findById(game.getAway_team_fk()).get() : null;
					return new KnockoutGameBean(game, homeTeam, awayTeam, predictionToGameIdMap.get(game.getId()));
				})
				.collect(Collectors.toList());
	}

	private void addFinals(List<KnockoutGameBean> finalsGames) {
		// Just to be sure, it should be impossible to not have 2 games (for now)
		if (finalsGames.size() == 2) {
			bracketLayout.addComponent(new RestFinalsGameLayout(singleLeagueServiceprovider, league, finalsGames.get(0)), 6, ROW_START + 7);
			bracketLayout.addComponent(new RestFinalsGameLayout(singleLeagueServiceprovider, league, finalsGames.get(1)), 6, ROW_START + 12);
		} else if (finalsGames.size() == 1) {
			bracketLayout.addComponent(new RestFinalsGameLayout(singleLeagueServiceprovider, league, finalsGames.get(0)), 6, ROW_START + 7);
		} else {
			// Meh...
		}
	}

	public void addRowsToColumn(
			List<KnockoutGameBean> games,
			int column,
			int spaceBetweenRows,
			int counterStart) {
		int counter = ROW_START + counterStart;
		for (KnockoutGameBean game : games) {
			if (FifaWorldCup2018Stages.EIGHTH_FINALS.toString().equals(game.getGame().getStage())) {
				bracketLayout.addComponent(new EightFinalsGameLayout(singleLeagueServiceprovider, league, game), column, counter);
			} else {
				bracketLayout.addComponent(new RestFinalsGameLayout(singleLeagueServiceprovider, league, game), column, counter);
			}
			counter = counter + spaceBetweenRows;
		}
	}

}
