package com.jeno.fantasyleague.ui.main.views.league.singleleague.knockoutstage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.model.Contestant;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
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

		Map<Long, KnockoutGameBean> knockoutGameBeans = fetchLeagueGames(league).stream()
				.collect(Collectors.toMap(bean -> bean.getGame().getId(), Function.identity()));
		List<TwoGamesWithNextId> gamesWithNextIdList = getTwoGamesWithNextIdObjects(knockoutGameBeans.values());
		TwoGamesWithNextId start = gamesWithNextIdList.stream()
				.filter(twoGames -> FifaWorldCup2018Stages.EIGHTH_FINALS.toString().equals(twoGames.getStage()))
				.sorted(Comparator.comparing(TwoGamesWithNextId::getEarliestDate))
				.findFirst()
				.get();
		ArrayListMultimap<String, KnockoutGameBean> gamesPerStage = ArrayListMultimap.create();
		Set<Long> addedGameIds = Sets.newHashSet();

		addGame(gamesPerStage, addedGameIds, start.firstBean);
		addGame(gamesPerStage, addedGameIds, start.secondBean);
		goForward(gamesPerStage, addedGameIds, gamesWithNextIdList, knockoutGameBeans, start);

		addRowsToColumn(gamesPerStage.get(FifaWorldCup2018Stages.EIGHTH_FINALS.toString()), 0, 2, 0);
		addEighthFinalsLines();
		addRowsToColumn(gamesPerStage.get(FifaWorldCup2018Stages.QUARTER_FINALS.toString()), 2, 4, 1);
		addQuarterFinalsLines();
		addRowsToColumn(gamesPerStage.get(FifaWorldCup2018Stages.SEMI_FINALS.toString()), 4, 8, 3);
		addSemiFinalsLines();

		addFinals(gamesPerStage.get(FifaWorldCup2018Stages.FINALS.toString()));
	}

	private void goForward(
			ArrayListMultimap<String, KnockoutGameBean> gamesPerStage,
			Set<Long> addedGameIds,
			List<TwoGamesWithNextId> gamesWithNextIdList,
			Map<Long, KnockoutGameBean> knockoutGameBeans,
			TwoGamesWithNextId toEvaluate) {
		Optional<TwoGamesWithNextId> optionalTwoGamesWithNextId = gamesWithNextIdList.stream()
				.filter(twoGamesWithNextId -> twoGamesWithNextId.getMatchingBean(toEvaluate.nextGameId).isPresent())
				.findFirst();
		if (optionalTwoGamesWithNextId.isPresent()) {
			TwoGamesWithNextId twoGamesWithNextId = optionalTwoGamesWithNextId.get();
			KnockoutGameBean matchingNextGame = twoGamesWithNextId.getMatchingBean(toEvaluate.nextGameId).get();
			addGame(gamesPerStage, addedGameIds, matchingNextGame);
			KnockoutGameBean otherGame = twoGamesWithNextId.firstBean.getGame().getId().equals(matchingNextGame.getGame().getId()) ?
					twoGamesWithNextId.secondBean :
					twoGamesWithNextId.firstBean;
			addGame(gamesPerStage, addedGameIds, otherGame);
			goBackward(gamesPerStage, addedGameIds, gamesWithNextIdList, knockoutGameBeans, otherGame);
			goForward(gamesPerStage, addedGameIds, gamesWithNextIdList, knockoutGameBeans, twoGamesWithNextId);
		} else {
			addGame(gamesPerStage, addedGameIds, knockoutGameBeans.get(toEvaluate.nextGameId));
		}
	}

	private void goBackward(
			ArrayListMultimap<String,KnockoutGameBean> gamesPerStage,
			Set<Long> addedGameIds,
			List<TwoGamesWithNextId> gamesWithNextIdList,
			Map<Long,KnockoutGameBean> knockoutGameBeans,
			KnockoutGameBean otherGame) {
		Optional<TwoGamesWithNextId> optionalTwoGamesWithNextId = gamesWithNextIdList.stream()
				.filter(twoGamesWithNextId -> twoGamesWithNextId.nextGameId.equals(otherGame.getGame().getId()))
				.findFirst();
		optionalTwoGamesWithNextId.ifPresent(twoGamesWithNextId -> {
			addGame(gamesPerStage, addedGameIds, twoGamesWithNextId.firstBean);
			addGame(gamesPerStage, addedGameIds, twoGamesWithNextId.secondBean);
			goBackward(gamesPerStage, addedGameIds, gamesWithNextIdList, knockoutGameBeans, twoGamesWithNextId.firstBean);
			goBackward(gamesPerStage, addedGameIds, gamesWithNextIdList, knockoutGameBeans, twoGamesWithNextId.secondBean);
		});
	}

	private void addGame(ArrayListMultimap<String, KnockoutGameBean> gamesPerStage, Set<Long> addedGameIds, KnockoutGameBean bean) {
		Long gameId = bean.getGame().getId();
		if (!addedGameIds.contains(gameId)) {
			addedGameIds.add(gameId);
			gamesPerStage.put(bean.getGame().getStage(), bean);
		}
	}

	public List<TwoGamesWithNextId> getTwoGamesWithNextIdObjects(Collection<KnockoutGameBean> knockoutGameBeans) {
		ArrayListMultimap<Long, KnockoutGameBean> gamesPerNextId = ArrayListMultimap.create();
		knockoutGameBeans.stream()
				.filter(bean -> Objects.nonNull(bean.getGame().getNext_game_fk()))
				.forEach(bean -> gamesPerNextId.put(bean.getGame().getNext_game_fk(), bean));
		return gamesPerNextId.asMap().entrySet().stream()
				.map(entry -> {
					List<KnockoutGameBean> sortedBeans = entry.getValue().stream()
							.sorted(Comparator.comparing(bean -> bean.getGame().getGameDateTime()))
							.collect(Collectors.toList());
					if (sortedBeans.size() == 2) {
						return new TwoGamesWithNextId(sortedBeans.get(0), sortedBeans.get(1), entry.getKey());
					} else {
						throw new RuntimeException("What, this is impossible");
					}
				})
				.collect(Collectors.toList());
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

	public List<KnockoutGameBean> fetchLeagueGames(League league) {
		List<Game> games = singleLeagueServiceprovider.getGameRepository().findByLeague(league);
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

	private class TwoGamesWithNextId {

		private final KnockoutGameBean firstBean;
		private final KnockoutGameBean secondBean;
		private final Long nextGameId;

		public TwoGamesWithNextId(KnockoutGameBean firstBean, KnockoutGameBean secondBean, Long nextGameId) {
			this.firstBean = firstBean;
			this.secondBean = secondBean;
			this.nextGameId = nextGameId;
		}

		public String getStage() {
			if (!firstBean.getGame().getStage().equals(secondBean.getGame().getStage())) {
				throw new RuntimeException("What???? Impossibru");
			}
			return firstBean.getGame().getStage();
		}

		public LocalDateTime getEarliestDate() {
			return firstBean.getGame().getGameDateTime();
		}

		public Optional<KnockoutGameBean> getMatchingBean(Long id) {
			if (firstBean.getGame().getId().equals(id)) {
				return Optional.of(firstBean);
			} else if (secondBean.getGame().getId().equals(id)) {
				return Optional.of(secondBean);
			} else {
				return Optional.empty();
			}
		}

	}

}
