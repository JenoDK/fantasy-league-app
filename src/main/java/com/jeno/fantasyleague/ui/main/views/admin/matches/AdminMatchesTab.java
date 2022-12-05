package com.jeno.fantasyleague.ui.main.views.admin.matches;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.common.tabsheet.LazyTabComponent;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchPredictionBean;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.reactivex.Observable;

public class AdminMatchesTab extends LazyTabComponent {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;
	private final Optional<League> optionalLeague;

	private VerticalLayout layout;
	private MatchGrid matchGrid;

	public AdminMatchesTab(SingleLeagueServiceProvider singleLeagueServiceProvider, Optional<League> optionalLeague) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
		this.optionalLeague = optionalLeague;

		initLayout();
	}

	private void initLayout() {
		layout = new VerticalLayout();
		layout.setMaxWidth("1200px");
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.setMargin(false);
		layout.setPadding(false);

		if (optionalLeague.isPresent()) {
			matchGrid = new MatchGrid(singleLeagueServiceProvider, singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(optionalLeague.get()), true);
			matchGrid.setMatches(getMatches(optionalLeague.get()));
			matchGrid.scoreChanged().subscribe(this::updateGameScoresGlobally);
			layout.add(matchGrid);
		} else {
			layout.add(new Label("Admin user has not joined a league yet"));
		}

		add(layout);
	}

	private List<MatchBean> getMatches(League league) {
		List<Game> games = singleLeagueServiceProvider.getGameRepository().findByLeague(league).stream()
				.sorted(Comparator.comparing(Game::getGameDateTime))
				.collect(Collectors.toList());
		Map<Long, Prediction> predictions = singleLeagueServiceProvider.getPredictionRepository()
				.findByLeagueAndUserAndJoinGames(league, singleLeagueServiceProvider.getLoggedInUser()).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity()));
		Map<Long, Contestant> contestantMap = singleLeagueServiceProvider.getContestantRepository().findByLeague(league).stream()
				.collect(Collectors.toMap(Contestant::getId, Function.identity()));
		List<MatchBean> matches = games.stream()
				.map(game -> new MatchBean(
						predictions.get(game.getId()),
						contestantMap.get(game.getHome_team_fk()),
						contestantMap.get(game.getAway_team_fk()),
						null,
						null,
						0.0,
						false,
						league))
				.collect(Collectors.toList());
		return matches;
	}

	public void updateGameScoresGlobally(MatchPredictionBean matchPredictionBean) {
		if (singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(matchPredictionBean.getLeague())) {
			List<Game> allGamesWithMatchNumber = singleLeagueServiceProvider.getGameRepository().findByMatchNumber(matchPredictionBean.getGame().getMatchNumber());
			allGamesWithMatchNumber.forEach(matchPredictionBean::setGameScoresAndGetGameModelItem);
			allGamesWithMatchNumber.forEach(game -> {
				if (SoccerCupStages.GROUP_PHASE.toString().equals(game.getStage())) {
					singleLeagueServiceProvider.getGameService().updateGroupStageGameScores(List.of(game));
				} else {
					singleLeagueServiceProvider.getGameService().updateKnockoutStageScore(game);
				}
			});
			// Do the set scores action on the bean as well for UI update
			matchPredictionBean.setGameScoresAndGetGameModelItem();
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"));
		}
	}
}
