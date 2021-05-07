package com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart;

import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.img;
import static j2html.TagCreator.span;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jeno.fantasyleague.backend.model.Contestant;
import com.jeno.fantasyleague.backend.model.Game;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.DataRole;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.IconColor;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.ScoreChartData;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.ScoreChartDataPerDate;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.ScoreChartModel;
import com.jeno.fantasyleague.ui.main.views.league.singleleague.overview.chart.model.ScoreChartSerieData;
import com.jeno.fantasyleague.util.DateUtil;
import com.jeno.fantasyleague.util.DecimalUtil;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

@Tag("score-chart")
@NpmPackage(value = "google-charts", version = "^2.0.0")
@JsModule("./src/views/overview/score-chart.js")
public class ScoreChart extends PolymerTemplate<ScoreChartModel> {

	public static final double DEFAULT_TOP_SHOWN = 4d;
	private final List<UserScoreBean> userScoreBeans;
	private final Map<String, String> colorsPerCountry;
	private final User user;

	@Id("content")
	private Div content;

	public ScoreChart(List<UserScoreBean> userScoreBeans, Map<String, String> colorsPerCountry, User user) {
		this.userScoreBeans = userScoreBeans;
		this.colorsPerCountry = colorsPerCountry;
		this.user = user;
		getModel().setIconColors(IconColor.createList(colorsPerCountry));
	}

	@ClientCallable
	private void clientIsReady() {
		switch (user.getGraph_preference()) {
			case COLUMN:
				showHBarChart(DEFAULT_TOP_SHOWN);
				break;
			case COLUMN_FLAGS:
				showVBarChart(DEFAULT_TOP_SHOWN);
				break;
			case LINE:
				showLineChart(DEFAULT_TOP_SHOWN);
				break;
			default:
				showHBarChart(DEFAULT_TOP_SHOWN);
				break;
		}
	}

	private void setHighestScore(List<ScoreChartData> scoresPerUser) {
		scoresPerUser.stream()
				.mapToDouble(s -> s.getScores().stream()
						.mapToDouble(score -> score)
						.sum())
				// * 0.1 so that we have room to show the profile picture, therwise the svg gets cut off
				.max().ifPresentOrElse(value -> getModel().setHighestScore(value + (value * 0.1f)), () -> getModel().setHighestScore(0));
	}

	private List<ScorePerContestant> getScoresPerContestant(List<Contestant> contestantsSorted, List<Game> gamesSorted, UserScoreBean b) {
		Map<Contestant, Double> scorePerTeam = Maps.newHashMap();
		gamesSorted.forEach(g -> {
			Double score = b.getScoreForGame(g);
			if (g.getWinner_fk() != null) {
				fillTeamScore(scorePerTeam, score, g.getWinner());
			} else {
				score = DecimalUtil.round(score / 2, 1);
				fillTeamScore(scorePerTeam, score, g.getHome_team());
				fillTeamScore(scorePerTeam, score, g.getAway_team());
			}
		});
		return contestantsSorted.stream()
				.map(contestant -> new ScorePerContestant(contestant, scorePerTeam.containsKey(contestant) ? scorePerTeam.get(contestant) : 0d))
				.collect(Collectors.toList());
	}

	private void fillTeamScore(Map<Contestant, Double> scorePerTeam, Double score, Contestant team) {
		if (scorePerTeam.containsKey(team)) {
			scorePerTeam.put(team, scorePerTeam.get(team) + score);
		} else {
			scorePerTeam.put(team, score);
		}
	}

	private List<Double> getScoresGroupedByWinner(UserScoreBean b, List<Game> gamesWithNoWinner, Multimap<Long, Game> gamesPerWinner) {
		List<Double> scoresWithWinners = gamesPerWinner.asMap().entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.map(e -> b.getScoreForGames(Lists.newArrayList(e.getValue())))
//				.sorted(Comparator.reverseOrder())
				.collect(Collectors.toList());
		scoresWithWinners.add(0, b.getScoreForGames(gamesWithNoWinner));
		return scoresWithWinners;
	}

	private boolean userIsLoggedInUser(User user, UserScoreBean b) {
		return b.getUser().getId().equals(user.getId());
	}

	private String[] colors = new String[]{
			"#2a7fef",
			"#a772e5",
			"#e964c6",
			"#ff629b",
			"#ff766d",
			"#ff9844",
			"#efba2a"};

	private String getColor(int position) {
		return "#2a7fef";
//		if (position >= colors.length) {
//			return colors[position % colors.length];
//		} else {
//			return colors[position];
//		}
	}

	public void showHBarChart(double showTop) {
		List<Game> gamesSorted = getGamesSorted();
		List<Contestant> contestants = getContestantsSorted();
		// Actual chart data
		List<ScoreChartData> scoresPerUser = userScoreBeans.stream()
				.filter(b -> b.getPosition() <= showTop || userIsLoggedInUser(user, b))
				.map(b -> {
					double totalScore = DecimalUtil.round(b.getTotalScore(), 1);
					return new ScoreChartData(
							getDisplayName(b),
							b.getUser().getId().intValue(),
							b.getUser().getProfile_picture() != null,
							List.of(totalScore),
							b.getPosition(),
							List.of(
									"color: " + getColor(b.getPosition() - 1),
									getTooltip(totalScore, contestants, gamesSorted, b)));
				})
				.collect(Collectors.toList());
		getModel().setScoresPerUser(scoresPerUser);
		setHighestScore(scoresPerUser);
		getModel().setChartHeaders(List.of("Name", "Points"));
		getModel().setDataRoles(List.of(new DataRole("style"), new DataRole("tooltip")));
		getModel().setColors(List.of(colors));
		getElement().callJsFunction("drawHBarChart");
	}

	private String getDisplayName(UserScoreBean b) {
		return b.getPosition() + ". " + b.getUser().getUsername() + (userIsLoggedInUser(user, b) ? " (you)" : "");
	}

	private String getTooltip(double totalScore,List<Contestant> contestantsSorted, List<Game> gamesSorted, UserScoreBean b) {
		List<ScorePerContestant> scorePerContestant = getScoresPerContestant(contestantsSorted, gamesSorted, b).stream()
				.filter(s -> s.getScore() > 0)
				.sorted(Comparator.comparing(ScorePerContestant::getScore).reversed())
				.collect(Collectors.toList());
		String profilePictureSrc = b.getUser().getProfile_picture() != null ? "/profileImage?userPk=" + b.getUser().getId().toString() : "/images/default_profile_picture.png";
		return
				div(
						div(
								img().withSrc(profilePictureSrc).withStyle("width:45px;height:45px"),
								span(getDisplayName(b)).withStyle("display:table-cell; vertical-align:middle; padding-left: 4px")
						).withStyle("display: table;"),
						span("Total points: " + totalScore),
						table(
								tbody(
										each(scorePerContestant, s -> tr(
												td(img().withSrc(s.getTeam().getIcon_path()).withStyle("width:30px;")),
												td(DecimalUtil.round(s.getScore(), 1) + "")
										))
								)
						)
				).withClass("chart-tooltip")
				.renderFormatted();
	}

	private List<Contestant> getContestantsSorted() {
		return userScoreBeans.stream().findFirst()
				.map(bean -> bean.getGames().stream()
						.flatMap(g -> Stream.of(g.getHome_team(), g.getAway_team()).filter(Objects::nonNull))
						.distinct()
						.sorted(Comparator.comparing(Contestant::getId))
						.collect(Collectors.toList()))
				.orElse(List.of());
	}

	private List<Game> getGamesSorted() {
		return userScoreBeans.stream().findFirst()
				.map(bean -> bean.getGames().stream()
						.sorted(Comparator.comparing(Game::getGameDateTime))
						.collect(Collectors.toList()))
				.orElse(List.of());
	}

	public void showVBarChart(double showTop) {
		List<Game> gamesSorted = getGamesSorted();
		List<Contestant> contestants = getContestantsSorted();
		if (!userScoreBeans.isEmpty() && !contestants.isEmpty()) {
			// Series data for the chart
			List<ScoreChartSerieData> series = contestants.stream()
					.map(contestant -> {
						String color = colorsPerCountry.get(contestant.getIcon_path());
						return new ScoreChartSerieData(contestant.getId().intValue(), color, contestant.getIcon_path());
					})
					.collect(Collectors.toList());
			getModel().setSeriesData(series);
			// Actual chart data
			List<ScoreChartData> scoresPerUser = userScoreBeans.stream()
					// Top 3 and logged in user
					.filter(b -> b.getPosition() <= showTop || userIsLoggedInUser(user, b))
					.map(b -> new ScoreChartData(
							getDisplayName(b),
							b.getUser().getId().intValue(),
							b.getUser().getProfile_picture() != null,
							getScoresPerContestant(contestants, gamesSorted, b).stream().map(ScorePerContestant::getScore).collect(Collectors.toList()),
							b.getPosition(),
							List.of()))
					.collect(Collectors.toList());
			getModel().setScoresPerUser(scoresPerUser);

			setHighestScore(scoresPerUser);

			// Chart headers
			List<String> chartHeaders = contestants.stream()
					.map(contestant -> contestant.getId() + " " + contestant.getName())
					.collect(Collectors.toList());
			chartHeaders.add(0, "Name");
			getModel().setChartHeaders(chartHeaders);
			getModel().setDataRoles(List.of());
			getElement().callJsFunction("drawCountryFlagChart");
		} else {
			setVisible(false);
		}
	}

	public void showLineChart(double showTop) {
		LocalDateTime now = LocalDateTime.now();
		List<Game> gamesSorted = getGamesSorted().stream()
				.filter(g -> g.getGameDateTime().isBefore(now))
				.collect(Collectors.toList());
		List<UserScoreBean> usersToShow = userScoreBeans.stream()
				.filter(b -> b.getPosition() <= showTop || userIsLoggedInUser(user, b))
				.collect(Collectors.toList());
		List<ScoreChartDataPerDate> scoresPerDate = Lists.newArrayList();
		if (!gamesSorted.isEmpty()) {
			gamesSorted.stream().findFirst()
					.map(Game::getGameDateTime)
					.ifPresent(firstDate -> {
						if (gamesSorted.size() < 9) {
							for (int i = 0; i < gamesSorted.size(); i++) {
								List<Game> allGamesUpUntilNow = gamesSorted.subList(0, i + 1);
								String date = allGamesUpUntilNow.stream().reduce((one, two) -> two)
										.map(lastGame -> DateUtil.DATE_DAY_FORMATTER.format(lastGame.getGameDateTime()))
										.orElse("NA");
								List<Double> scores = usersToShow.stream()
										.map(u -> u.getScoreForGames(allGamesUpUntilNow))
										.collect(Collectors.toList());
								scoresPerDate.add(new ScoreChartDataPerDate(date, scores));
							}
						} else {
							int chunkSize = gamesSorted.size() / 8;
							List<List<Game>> gamesPartitioned = Lists.partition(gamesSorted, chunkSize);
							for (int i = 0; i < gamesPartitioned.size(); i++) {
								List<Game> allGamesUpUntilNow = gamesPartitioned.subList(0, i + 1).stream()
										.flatMap(List::stream)
										.sorted(Comparator.comparing(Game::getGameDateTime))
										.collect(Collectors.toList());
								String date = gamesPartitioned.get(i).stream().reduce((one, two) -> two)
										.map(lastGame -> DateUtil.DATE_DAY_FORMATTER.format(lastGame.getGameDateTime()))
										.orElse("NA");
								List<Double> scores = usersToShow.stream()
										.map(u -> u.getScoreForGames(allGamesUpUntilNow))
										.collect(Collectors.toList());
								scoresPerDate.add(new ScoreChartDataPerDate(date, scores));
							}
						}
					});
		} else {
			scoresPerDate.add(new ScoreChartDataPerDate(DateUtil.DATE_DAY_FORMATTER.format(now), usersToShow.stream().map(ignored -> 0d).collect(Collectors.toList())));
		}
		getModel().setScoresPerDate(scoresPerDate);
		List<String> headers = usersToShow.stream()
				.map(this::getDisplayName)
				.collect(Collectors.toList());
		headers.add(0, "Date");
		getModel().setChartHeaders(headers);
		getElement().callJsFunction("drawLineChart");
	}

	public class ScorePerContestant {
		private final Contestant team;
		private final Double score;

		public ScorePerContestant(Contestant team, Double score) {
			this.team = team;
			this.score = score;
		}

		public Contestant getTeam() {
			return team;
		}

		public Double getScore() {
			return score;
		}
	}

}
