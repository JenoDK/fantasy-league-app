package com.jeno.fantasyleague.ui.main.views.league;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.data.repository.ContestantGroupRepository;
import com.jeno.fantasyleague.data.repository.ContestantRepository;
import com.jeno.fantasyleague.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.data.repository.GameRepository;
import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.repository.LeagueSettingRepository;
import com.jeno.fantasyleague.data.repository.PredictionRepository;
import com.jeno.fantasyleague.data.repository.UserNotificationRepository;
import com.jeno.fantasyleague.data.security.SecurityHolder;
import com.jeno.fantasyleague.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.data.service.repo.contestant.ContestantService;
import com.jeno.fantasyleague.data.service.repo.game.GameService;
import com.jeno.fantasyleague.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.data.service.repo.user.UserService;
import com.jeno.fantasyleague.model.ContestantWeight;
import com.jeno.fantasyleague.model.Game;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.model.enums.NotificationType;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

	public UserLeagueScore getUserLeagueScore(League league, User user) {
		return leagueService.getTotalLeagueScoreForUser(league, user);
	}

	public double getUserLeaguePredictionScore(League league, Prediction prediction) {
		return leagueService.getPredictionScoreForUser(league, prediction, securityHolder.getUser());
	}

	public double getLeaguePredictionScoreForUser(League league, Prediction prediction, User user) {
		return leagueService.getPredictionScoreForUser(league, prediction, user);
	}

	public ApplicationEmailService getEmailService() {
		return emailService;
	}

	public LeagueSettingRepository getLeagueSettingRepository() {
		return leagueSettingRepository;
	}
}
