package com.jeno.fantasyleague.ui.main.views.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.Template;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.ui.main.views.league.SingleLeagueServiceProvider;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchPredictionBean;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class AdminModel {

	@Autowired
	private SingleLeagueServiceProvider singleLeagueServiceProvider;
	@Autowired
	private LeagueUserRepository leagueUserRepository;

	public Optional<League> getSingleLeagueForLoggedInUser(User user) {
		return leagueUserRepository.findByUser(user).stream()
				.map(LeagueUser::getLeague)
				.filter(League::getActive)
				.filter(l -> Template.FIFA_WORLD_CUP_2022.equals(l.getTemplate()))
				.findFirst();
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
