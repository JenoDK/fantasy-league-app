package com.jeno.fantasyleague.data.service.repo.league;

import java.util.Map;

import com.jeno.fantasyleague.data.repository.LeagueRepository;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.data.service.leaguetemplates.worldcup2018.FifaWorldCup2018Stages;
import com.jeno.fantasyleague.data.service.repo.contestantweight.ContestantWeightService;
import com.jeno.fantasyleague.data.service.repo.prediction.PredictionService;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.Prediction;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		league.getUsers().add(user);
		League updatedLeague = leagueRepo.saveAndFlush(league);

		contestantWeightService.addDefaultContestantWeights(updatedLeague, user);
		predictionService.addDefaultPredictions(league, user);
	}

	@Override
	public Map<FifaWorldCup2018Stages, Double> getTotalLeagueScoreForUser(League league, User user) {
		// Run template bean
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		return templateServiceBean.calculateTotalUserScore(league, user);
	}

	@Override
	public double getPredictionScoreForUser(League league, Prediction prediction, User user) {
		// Run template bean
		LeagueTemplateService templateServiceBean = beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class);
		return templateServiceBean.calculateScoreOfPrediction(league, prediction, user);
	}

}
