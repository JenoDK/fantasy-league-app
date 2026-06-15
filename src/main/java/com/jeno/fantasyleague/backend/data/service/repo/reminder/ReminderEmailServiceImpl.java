package com.jeno.fantasyleague.backend.data.service.repo.reminder;

import com.jeno.fantasyleague.backend.data.repository.ContestantWeightRepository;
import com.jeno.fantasyleague.backend.data.repository.GameRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.LeagueUserRepository;
import com.jeno.fantasyleague.backend.data.repository.PredictionRepository;
import com.jeno.fantasyleague.backend.data.repository.ReminderEmailRepository;
import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.ContestantWeight;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.LeagueUser;
import com.jeno.fantasyleague.backend.model.Prediction;
import com.jeno.fantasyleague.backend.model.ReminderEmail;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.ReminderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Transactional
public class ReminderEmailServiceImpl implements ReminderEmailService {

	private static final Logger LOG = LogManager.getLogger(ReminderEmailServiceImpl.class.getName());

	private static final ZoneId BRUSSELS = ZoneId.of("Europe/Brussels");
	private static final DateTimeFormatter EMAIL_DATE_TIME = DateTimeFormatter.ofPattern("dd/MM HH:mm");
	private static final String LOGIN_URL = "https://jenodk.com/fantasy-league";
	// Predictions digest looks at matches starting within this window.
	private static final Duration PREDICTION_WINDOW = Duration.ofHours(48);

	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private LeagueUserRepository leagueUserRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private PredictionRepository predictionRepository;
	@Autowired
	private ContestantWeightRepository contestantWeightRepository;
	@Autowired
	private ReminderEmailRepository reminderEmailRepository;
	@Autowired
	private ApplicationEmailService emailService;

	// Hourly; the window logic + idempotency log make the exact cadence non-critical.
	@Scheduled(cron = "0 0 */1 * * *", zone = "Europe/Brussels")
	@Override
	public void sendStockReminders() {
		activeLeagues().forEach(this::sendStockRemindersForLeague);
	}

	private void sendStockRemindersForLeague(League league) {
		LocalDateTime start = league.getLeague_starting_date();
		if (start == null) {
			return;
		}
		// Stored datetimes are UTC.
		long hoursUntilStart = Duration.between(Instant.now(), start.toInstant(ZoneOffset.UTC)).toHours();
		ReminderType stage;
		if (hoursUntilStart > 24 && hoursUntilStart <= 48) {
			stage = ReminderType.STOCK_48H;
		} else if (hoursUntilStart > 0 && hoursUntilStart <= 24) {
			stage = ReminderType.STOCK_24H;
		} else {
			// Already started, or still more than 48h away - nothing to send yet.
			return;
		}

		String subject = "FIFA World Cup 2026 - Buy your stocks";
		String body = "You haven't bought your stocks yet in " + league.getName() + ".\n\n" +
				"Stock buying closes when the league starts on " + formatBrussels(start) + ".\n" +
				"Pick your teams before then.\n\n" +
				"Log in to " + LOGIN_URL + " to buy your stocks.";

		members(league).stream()
				.filter(User::isReminder_emails_enabled)
				.filter(user -> !hasBoughtStocks(user, league))
				.filter(user -> !reminderEmailRepository.existsForUserAndTypeAndReference(user, stage, league.getId()))
				.forEach(user -> {
					try {
						emailService.sendEmail(subject, body, user);
						reminderEmailRepository.save(new ReminderEmail(user, league, stage, league.getId()));
					} catch (Exception e) {
						LOG.warn("Failed to send stock reminder ({}) to {}", stage, user.getEmail(), e);
					}
				});
	}

	// Daily at 09:00 Belgian local time.
	@Scheduled(cron = "0 0 9 * * *", zone = "Europe/Brussels")
	@Override
	public void sendPredictionReminders() {
		activeLeagues().forEach(this::sendPredictionRemindersForLeague);
	}

	private void sendPredictionRemindersForLeague(League league) {
		Instant now = Instant.now();
		Instant windowEnd = now.plus(PREDICTION_WINDOW);
		List<Game> upcomingGames = gameRepository.findByLeague(league).stream()
				.filter(game -> game.getGameDateTime() != null)
				.filter(game -> {
					Instant kickOff = game.getGameDateTime().toInstant(ZoneOffset.UTC);
					return kickOff.isAfter(now) && !kickOff.isAfter(windowEnd);
				})
				.collect(Collectors.toList());
		if (upcomingGames.isEmpty()) {
			return;
		}

		members(league).stream()
				.filter(User::isReminder_emails_enabled)
				.forEach(user -> sendPredictionDigest(league, user, upcomingGames));
	}

	private void sendPredictionDigest(League league, User user, List<Game> upcomingGames) {
		Map<Long, Prediction> predictionsByGameId = predictionRepository.findByLeagueAndUserAndJoinGames(league, user).stream()
				.collect(Collectors.toMap(Prediction::getGame_fk, Function.identity(), (a, b) -> a));
		Set<Long> alreadyReminded = reminderEmailRepository.findByUserAndType(user, ReminderType.PREDICTION).stream()
				.map(ReminderEmail::getReference_id)
				.collect(Collectors.toSet());

		List<Game> toRemind = upcomingGames.stream()
				.filter(game -> !alreadyReminded.contains(game.getId()))
				.filter(game -> isNotFilledIn(predictionsByGameId.get(game.getId())))
				.sorted(Comparator.comparing(Game::getGameDateTime))
				.collect(Collectors.toList());
		if (toRemind.isEmpty()) {
			return;
		}

		StringBuilder body = new StringBuilder("These matches start soon and you haven't predicted them yet:\n\n");
		toRemind.forEach(game -> body
				.append("- ")
				.append(teamName(game.getHome_team(), game.getHome_team_placeholder()))
				.append(" vs ")
				.append(teamName(game.getAway_team(), game.getAway_team_placeholder()))
				.append(" — ")
				.append(formatBrussels(game.getGameDateTime()))
				.append("\n"));
		body.append("\nLog in to ").append(LOGIN_URL).append(" to fill them in before kickoff.");

		try {
			emailService.sendEmail("FIFA World Cup 2026 - Predictions to fill in", body.toString(), user);
			toRemind.forEach(game -> reminderEmailRepository.save(
					new ReminderEmail(user, league, ReminderType.PREDICTION, game.getId())));
		} catch (Exception e) {
			LOG.warn("Failed to send prediction reminder to {}", user.getEmail(), e);
		}
	}

	private boolean hasBoughtStocks(User user, League league) {
		return contestantWeightRepository.findByUserAndLeague(user, league).stream()
				.map(ContestantWeight::getWeight)
				.filter(Objects::nonNull)
				.anyMatch(weight -> weight > 0);
	}

	private boolean isNotFilledIn(Prediction prediction) {
		return prediction == null
				|| (prediction.getHome_team_score() == null && prediction.getAway_team_score() == null);
	}

	private List<League> activeLeagues() {
		return leagueRepository.findAll().stream()
				.filter(league -> Boolean.TRUE.equals(league.getActive()))
				.collect(Collectors.toList());
	}

	private List<User> members(League league) {
		return leagueUserRepository.findByLeague(league).stream()
				.map(LeagueUser::getUser)
				.collect(Collectors.toList());
	}

	private String teamName(Contestant team, String placeholder) {
		if (team != null && team.getName() != null) {
			return team.getName();
		}
		return placeholder != null ? placeholder : "TBD";
	}

	private String formatBrussels(LocalDateTime utcDateTime) {
		return utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(BRUSSELS).format(EMAIL_DATE_TIME);
	}
}
