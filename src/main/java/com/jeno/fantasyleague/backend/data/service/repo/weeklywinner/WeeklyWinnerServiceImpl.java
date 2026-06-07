package com.jeno.fantasyleague.backend.data.service.repo.weeklywinner;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.repository.WeeklyWinnerRepository;
import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.data.service.repo.league.UserLeagueScore;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.WeeklyWinner;
import com.jeno.fantasyleague.backend.model.enums.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class WeeklyWinnerServiceImpl implements WeeklyWinnerService {

	private static final Logger LOG = LogManager.getLogger(WeeklyWinnerServiceImpl.class.getName());

	// Scores are doubles; treat anything within this margin of the max as tied for first place.
	private static final double TIE_EPSILON = 1e-9;

	@Autowired
	private LeagueService leagueService;
	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private WeeklyWinnerRepository weeklyWinnerRepository;
	@Autowired
	private ApplicationEmailService emailService;

	// Every Sunday at 20:00 Belgian local time (handles CET/CEST DST automatically).
	@Scheduled(cron = "0 0 20 * * SUN", zone = "Europe/Brussels")
	@Override
	public void recordWeeklyWinners() {
		List<League> leagues = leagueRepository.findByTemplate(Template.FIFA_WORLD_CUP_2026).stream()
				.filter(League::getActive)
				.collect(Collectors.toList());
		LOG.info("Recording weekly winners for {} active World Cup 2026 league(s)", leagues.size());
		leagues.forEach(this::recordForLeague);
	}

	private void recordForLeague(League league) {
		List<UserLeagueScore> scores = leagueService.getTotalLeagueScores(league);
		double topScore = scores.stream().mapToDouble(this::totalScore).max().orElse(0.0);
		if (topScore <= 0.0) {
			// Nobody has scored yet (e.g. before the tournament) - nothing meaningful to announce.
			return;
		}
		Set<User> winners = scores.stream()
				.filter(score -> totalScore(score) >= topScore - TIE_EPSILON)
				.map(UserLeagueScore::getUser)
				.collect(Collectors.toSet());

		WeeklyWinner weeklyWinner = new WeeklyWinner();
		weeklyWinner.setLeague(league);
		weeklyWinner.setWinners(winners);
		weeklyWinner.setTopScore(topScore);
		weeklyWinner.setAnnouncedAt(LocalDateTime.now());
		weeklyWinnerRepository.saveAndFlush(weeklyWinner);
		LOG.info("Weekly winner for league '{}': {} with {} points", league.getName(),
				winners.stream().map(User::getUsername).collect(Collectors.joining(", ")), topScore);

		emailAllMembers(league, winners, topScore);
	}

	private void emailAllMembers(League league, Set<User> winners, double topScore) {
		String names = winners.stream().map(User::getUsername).sorted().collect(Collectors.joining(", "));
		String scoreText = topScore == Math.floor(topScore) ? String.valueOf((long) topScore) : String.valueOf(topScore);
		String announcement = winners.size() == 1
				? names + " is this week's leader in " + league.getName() + " with " + scoreText + " points!"
				: "Joint leaders this week in " + league.getName() + " with " + scoreText + " points: " + names;
		String subject = "FIFA World Cup 2026 - Winner of the week";
		String body = announcement + "\n\nLog in to https://jenodk.com/fantasy-league to see the full standings.";

		leagueUserRepository.findByLeague(league).stream()
				.map(LeagueUser::getUser)
				.forEach(user -> {
					try {
						emailService.sendEmail(subject, body, user);
					} catch (Exception e) {
						LOG.warn("Failed to send weekly winner email to {}", user.getEmail(), e);
					}
				});
	}

	private double totalScore(UserLeagueScore score) {
		return score.getScoresPerGame().values().stream().mapToDouble(Double::doubleValue).sum();
	}

	@Override
	public Optional<WeeklyWinner> getLatestUnseen(League league, LeagueUser leagueUser) {
		Optional<WeeklyWinner> latest = weeklyWinnerRepository.findTopByLeagueOrderByIdDesc(league);
		if (latest.isEmpty()) {
			return Optional.empty();
		}
		Long lastSeenId = leagueUser.getLast_seen_weekly_winner_id();
		if (lastSeenId != null && lastSeenId.equals(latest.get().getId())) {
			return Optional.empty();
		}
		return latest;
	}

}
