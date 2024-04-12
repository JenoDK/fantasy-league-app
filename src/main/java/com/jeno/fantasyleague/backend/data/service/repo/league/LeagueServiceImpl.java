package com.jeno.fantasyleague.backend.data.service.repo.league;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.backend.data.service.repo.contestantweight.ContestantWeightService;
import com.jeno.fantasyleague.backend.data.service.repo.prediction.PredictionService;
import com.jeno.fantasyleague.backend.model.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.jeno.fantasyleague.FantasyLeagueApplicationRunner.DEFAULT_LUEAGUE_GUID;

@Transactional
@Component
public class LeagueServiceImpl implements LeagueService {

	@Autowired
	private ContestantWeightService contestantWeightService;
	@Autowired
	private PredictionService predictionService;
	@Autowired
	private LeagueRepository leagueRepo;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private BeanFactory beanFactory;

	@Override
	public League addLeague(League league, User user) {
		League newLeague = leagueRepo.save(league);

		// Run template bean
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		templateServiceBean.run(newLeague, user);

		addUserToLeague(newLeague, user);

		return leagueRepo.findById(newLeague.getId()).get();
	}

	@Override
	public void addUserToLeague(League league, User user) {
		LeagueUser leagueUser = new LeagueUser();
		leagueUser.setLeague(league);
		leagueUser.setUser(user);
		leagueUser.setShow_help(true);
		leagueUser.setHelp_stage(LeagueUser.HelpStage.INTRO);
		leagueUserRepository.save(leagueUser);
		leagueRepo.findById(league.getId()).ifPresent(updatedLeague -> {
			contestantWeightService.addDefaultContestantWeights(updatedLeague, user);
			predictionService.addDefaultPredictions(league, user);
		});
	}

	@Override
	public List<UserLeagueScore> getTotalLeagueScores(League league) {
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		return templateServiceBean.calculateTotalUserScores(league);
	}

	@Override
	public double getPredictionScoreForUser(League league, Prediction prediction, User user) {
		// Run template bean
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		return templateServiceBean.calculateScoreOfPrediction(league, prediction, user);
	}

	@Override
	public Map<Long, Double> getPredictionScoresForUser(
			League league,
			List<Prediction> predictionsWithJoinedGames,
			List<ContestantWeight> contestantWeights) {
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		return templateServiceBean.calculateScoresForUser(league, predictionsWithJoinedGames, contestantWeights);
	}

	@Override
	public void addUserToDefaultLeague(User user) {
		leagueRepo.findByGuid(DEFAULT_LUEAGUE_GUID).ifPresent(defaultLeague -> addUserToLeague(defaultLeague, user));
	}

}
