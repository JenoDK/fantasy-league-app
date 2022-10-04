package com.jeno.fantasyleague.ui.main.views.league;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.backend.data.repository.EmailLeagueInviteRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.repository.PredictionRepository;
import com.jeno.fantasyleague.backend.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.SoccerCupStages;
import com.jeno.fantasyleague.backend.data.service.repo.contestant.ContestantService;
import com.jeno.fantasyleague.backend.data.service.repo.game.GameService;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.data.service.repo.user.UserService;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueSetting;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.backend.model.enums.NotificationType;
import com.jeno.fantasyleague.resources.Resources;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.matches.MatchPredictionBean;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.reactivex.Observable;

@SpringComponent
public class SingleLeagueServiceProvider {

	@Autowired
	private UserService userService;
	@Autowired
	private GameService gameService;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private ContestantService contestantService;
	@Autowired
	private ApplicationEmailService emailService;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContestantRepository contestantRepository;
	@Autowired
	private ContestantGroupRepository contestantGroupRepository;
	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private PredictionRepository predictionRepository;
	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private UserNotificationRepository userNotificationRepository;
	@Autowired
	private LeagueSettingRepository leagueSettingRepository;
	@Autowired
	private EmailLeagueInviteRepository emailLeagueInviteRepository;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SecurityHolder securityHolder;
	@Autowired
	private BeanFactory beanFactory;

	public void predictionChanged(Observable<MatchPredictionBean> predictionChanged, Consumer<Prediction> postConsume) {
		predictionChanged
				.subscribe(bean -> {
					Prediction prediction = bean.setPredictionScoresAndGetModelItem();
					if (LocalDateTime.now(ZoneId.of("Europe/Brussels")).isBefore(prediction.getGame().getGameDateTime())) {
						getPredictionRepository().saveAndFlush(prediction);
						postConsume.accept(prediction);
					} else {
						Notification.show(Resources.getMessage("toLateToUpdatePrediction"));
					}
					if (prediction.getHome_team_score().equals(prediction.getAway_team_score()) && Objects.isNull(prediction.getWinner()) && !SoccerCupStages.GROUP_PHASE.toString().equals(prediction.getGame().getStage())) {
						Notification.show("Be sure to select a winner");
					}
				});
	}

	public void updateGameScore(MatchPredictionBean matchPredictionBean) {
		if (loggedInUserIsLeagueAdmin(matchPredictionBean.getLeague())) {
			Game game = matchPredictionBean.setGameScoresAndGetGameModelItem();
			if (SoccerCupStages.GROUP_PHASE.toString().equals(game.getStage())) {
				getGameService().updateGroupStageGameScores(List.of(game));
			} else {
				getGameService().updateKnockoutStageScore(game);
			}
		} else {
			Notification.show(Resources.getMessage("adminRightsRevoked"));
		}
	}

	public void deactivateLeague(League league) {
		league.setActive(false);
		leagueRepository.saveAndFlush(league);
	}

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	public LeagueRepository getLeagueRepository() {
		return leagueRepository;
	}

	public LeagueUserRepository getLeagueUserRepository() {
		return leagueUserRepository;
	}

	public UserService getUserService() {
		return userService;
	}

	public GameService getGameService() {
		return gameService;
	}

	public ContestantService getContestantService() {
		return contestantService;
	}

	public ContestantGroupRepository getContestantGroupRepository() {
		return contestantGroupRepository;
	}

	public ContestantRepository getContestantRepository() {
		return contestantRepository;
	}

	public ContestantWeightRepository getContestantWeightRepository() {
		return contestantWeightRepository;
	}

	public PredictionRepository getPredictionRepository() {
		return predictionRepository;
	}

	public EmailLeagueInviteRepository getEmailLeagueInviteRepository() {
		return emailLeagueInviteRepository;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public UserNotification createLeagueInviteUserNotification(User user, League league) {
		UserNotification notification = new UserNotification();
		notification.setUser(user);
		notification.setMessage(Resources.getMessage("leagueInvite", league.getName()));
		notification.setReference_id(league.getId());
		notification.setReference_table("league");
		notification.setNotification_type(NotificationType.LEAGUE_INVITE);
		return userNotificationRepository.saveAndFlush(notification);
	}

	public List<User> getUsersWithPendingInvite(League league, List<User> leagueUsers) {
		Set<Long> leagueUSerIds = leagueUsers.stream().map(User::getId).collect(Collectors.toSet());
		return userNotificationRepository
				.findByNotificationTypeAndReferenceIdAndJoinUsers(NotificationType.LEAGUE_INVITE, league.getId()).stream()
						.filter(user -> !leagueUSerIds.contains(user.getUser().getId()))
						.map(UserNotification::getUser)
						.collect(Collectors.toList());
	}

	public List<ContestantWeight> getContestantWeights(League league) {
		return contestantWeightRepository.findByUserAndLeagueAndJoinContestant(securityHolder.getUser(), league);
	}

	public User getLoggedInUser() {
		return securityHolder.getUser();
	}

	public SecurityHolder getSecurityHolder() {
		return securityHolder;
	}

	public boolean loggedInUserIsLeagueCreator(League league) {
		return league.getCreatedBy().getId().equals(securityHolder.getUser().getId());
	}

	public boolean loggedInUserIsLeagueAdmin(League league) {
		return userIsLeagueAdmin(league, securityHolder.getUser());
	}

	public boolean userIsLeagueAdmin(League league, User user) {
		return leagueRepository.fetchLeagueOwners(league.getId()).stream()
				.anyMatch(owner -> owner.getId().equals(user.getId()));
	}

	public void promoteUserToLeagueOwner(League league, User user) {
		Set<User> owners = Sets.newHashSet(leagueRepository.fetchLeagueOwners(league.getId()));
		owners.add(user);
		league.setOwners(owners);
		leagueRepository.saveAndFlush(league);
	}

	public void demoteUserToLeagueNonOwner(League league, User user) {
		Set<User> owners = Sets.newHashSet(leagueRepository.fetchLeagueOwners(league.getId()));
		owners.removeIf(owner -> owner.getId().equals(user.getId()));
		if (owners.size() >= 1) {
			league.setOwners(owners);
			leagueRepository.saveAndFlush(league);
		}
	}

	public List<Prediction> getLoggedInUserPredictions(List<Game> games) {
		return predictionRepository.findByGameInAndUser(games, securityHolder.getUser());
	}

	public LeagueTemplateService getLeagueTemplateServiceBean(League league) {
		return beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
	}

	public List<UserLeagueScore> getUserLeagueScores(League league) {
		return leagueService.getTotalLeagueScores(league);
	}

	public double getLeaguePredictionScoreForUser(League league, Prediction prediction, User user) {
		return leagueService.getPredictionScoreForUser(league, prediction, user);
	}

	public Map<Long, Double> getLeaguePredictionScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights) {
		return leagueService.getPredictionScoresForUser(league, predictionsWithJoinedGames, contestantWeights);
	}

	public ApplicationEmailService getEmailService() {
		return emailService;
	}

	public LeagueSettingRepository getLeagueSettingRepository() {
		return leagueSettingRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}
}
