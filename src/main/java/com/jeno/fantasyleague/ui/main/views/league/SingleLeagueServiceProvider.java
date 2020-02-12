package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.backend.data.repository.PredictionRepository;
import com.jeno.fantasyleague.backend.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.repo.contestant.ContestantService;
import com.jeno.fantasyleague.backend.data.service.repo.game.GameService;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.data.service.repo.user.UserService;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.UserNotification;
import com.jeno.fantasyleague.backend.model.enums.NotificationType;
import com.jeno.fantasyleague.security.SecurityHolder;
import com.vaadin.flow.spring.annotation.SpringComponent;

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
	private UserNotificationRepository userNotificationRepository;
	@Autowired
	private LeagueSettingRepository leagueSettingRepository;

	@Autowired
	private SecurityHolder securityHolder;
	@Autowired
	private BeanFactory beanFactory;

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	public LeagueRepository getLeagueRepository() {
		return leagueRepository;
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

	public UserNotification createLeagueInviteUserNotification(User user, League league) {
		UserNotification notification = new UserNotification();
		notification.setUser(user);
		notification.setMessage(league.getName() + " League invite");
		notification.setReference_id(league.getId());
		notification.setReference_table("league");
		notification.setNotification_type(NotificationType.LEAGUE_INVITE);
		return userNotificationRepository.saveAndFlush(notification);
	}

	public List<User> getUsersWithPendingInvite(League league) {
		return userNotificationRepository
				.findByNotificationTypeAndReferenceIdAndJoinUsers(NotificationType.LEAGUE_INVITE, league.getId()).stream()
						.map(UserNotification::getUser)
						.collect(Collectors.toList());
	}

	public List<ContestantWeight> getContestantWeights(League league) {
		return contestantWeightRepository.findByUserAndLeagueAndJoinContestant(securityHolder.getUser(), league);
	}

	public User getLoggedInUser() {
		return securityHolder.getUser();
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

	public double getUserLeaguePredictionScore(League league, Prediction prediction) {
		return leagueService.getPredictionScoreForUser(league, prediction, securityHolder.getUser());
	}

	public double getLeaguePredictionScoreForUser(League league, Prediction prediction, User user) {
		return leagueService.getPredictionScoreForUser(league, prediction, user);
	}

	public Map<Long, Double> getLeaguePredictionScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights,
			User user) {
		return leagueService.getPredictionScoresForUser(league, predictionsWithJoinedGames, contestantWeights, user);
	}

	public ApplicationEmailService getEmailService() {
		return emailService;
	}

	public LeagueSettingRepository getLeagueSettingRepository() {
		return leagueSettingRepository;
	}
}
