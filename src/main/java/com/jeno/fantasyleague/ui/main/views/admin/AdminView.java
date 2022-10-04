package com.jeno.fantasyleague.ui.main.views.admin;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchBean;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchGrid;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchPredictionBean;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.reactivex.Observable;

@SpringComponent
@UIScope
public class AdminView {

	private final SingleLeagueServiceProvider singleLeagueServiceProvider;

	private boolean init = false;
	private VerticalLayout rootLayout;
	private VerticalLayout layout;
	private MatchGrid matchGrid;

	@Autowired
	public AdminView(SingleLeagueServiceProvider singleLeagueServiceProvider) {
		this.singleLeagueServiceProvider = singleLeagueServiceProvider;
	}

	private void initLayout(Optional<League> optionalLeague) {
		rootLayout = new VerticalLayout();

		layout = new VerticalLayout();
		layout.setMaxWidth("1200px");
		layout.setAlignItems(FlexComponent.Alignment.CENTER);
		layout.setMargin(false);
		layout.setPadding(false);

		if (optionalLeague.isPresent()) {
			matchGrid = new MatchGrid(singleLeagueServiceProvider, singleLeagueServiceProvider.loggedInUserIsLeagueAdmin(optionalLeague.get()), true);
			matchGrid.setMatches(getMatches(optionalLeague.get()));
			layout.add(matchGrid);
			Observable<MatchPredictionBean> matchPredictionBeanObservable = matchGrid.scoreChanged();
		} else {
			layout.add(new Label("Admin user has not joined a league yet"));
		}

		rootLayout.add(layout);
		rootLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}

	public VerticalLayout getLayout(Optional<League> optionalLeague) {
		if (!init) {
			initLayout(optionalLeague);
		}
		return rootLayout;
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

	public Observable<MatchPredictionBean> scoreChanged() {
		if (matchGrid != null) {
			return matchGrid.scoreChanged();
		}
		return Observable.never();
	}
}
