package com.jeno.fantasyleague.backend.data.service.repo.contestantweight;

import com.jeno.fantasyleague.backend.data.repository.ContestantRepository;
import com.jeno.fantasyleague.backend.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Component
public class ContestantWeightServiceImpl implements ContestantWeightService {

	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private ContestantRepository contestantRepository;

	@Override
	public List<ContestantWeight> addDefaultContestantWeights(League league, User user) {
		List<ContestantWeight> contestantWeights = contestantRepository.findByLeague(league).stream()
				.map(contestant -> createDefaultContestantWeight(contestant, league, user))
				.collect(Collectors.toList());
		return contestantWeightRepository.saveAll(contestantWeights);
	}

	private ContestantWeight createDefaultContestantWeight(Contestant contestant, League league, User user) {
		ContestantWeight contestantWeight = new ContestantWeight();
		contestantWeight.setContestant(contestant);
		contestantWeight.setUser(user);
		contestantWeight.setLeague(league);
		contestantWeight.setWeight(0);
		return contestantWeight;
	}
}
