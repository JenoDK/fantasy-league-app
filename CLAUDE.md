# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A fantasy sports prediction app ("wk-app" / fantasy-league) where users join leagues built
from tournament templates (UEFA Euro, FIFA World Cup), predict match outcomes, and are scored
against real results. Built with **Vaadin 14 (Flow) + Spring Boot 2.5**, Java 17, MySQL, and
Liquibase. Frontend uses Vaadin's Polymer/lit web components (npm + webpack), not a separate SPA.

## Commands

Build/run use Gradle (`./gradlew`). Java 17 is required (`.java-version` pins 17).

```bash
./gradlew vaadinCompile        # set up widgetset + styling (run before first bootRun)
./gradlew bootRun              # run Spring Boot (dev profile by default, port 8080)
./gradlew vaadinRun            # vaadinCompile + bootRun in one step
./gradlew clean build          # full build incl. tests
./gradlew test                 # run tests
./gradlew test --tests com.jeno.fantasyleague.FantasyLeagueApplicationTest   # single test class
./gradlew bootJar -Pvaadin.productionMode   # production-mode bootable jar
```

Production builds: `build_bootable_jar_app.sh` (jar), `build_aws_app.sh` (Elastic Beanstalk zip),
`release_hetzner.sh` (builds with `hetzner` profile and scp's the jar to the server).

### Local setup

Requires a MySQL DB and `src/main/resources/application-dev.properties` with
`spring.datasource.{username,password,url}` (see README). The `dev` profile is active by default.
Liquibase manages the schema (`spring.jpa.hibernate.ddl-auto=none`) — schema changes go through
changelog files, not entity-driven DDL.

## Architecture

### Layering (`com.jeno.fantasyleague`)
- `backend/model` — JPA entities. Core graph: `League` → `Game`/`Contestant`/`ContestantGroup`,
  `User` ↔ `League` via `LeagueUser`, and `Prediction` (a user's predicted outcome for a `Game`).
  Entities extend `audit/DateAudit` or `audit/UserAudit` for created/modified auditing.
- `backend/data/repository` — Spring Data JPA repositories.
- `backend/data/service/repo/*` — one service package per aggregate (league, game, prediction,
  user, contestant, contestantweight, leaguemessage), each with an interface + `*Impl`.
- `ui/*` — Vaadin views and components. `ui/common/*` holds reusable field/grid/image/window widgets.
- `security/*` — Spring Security config (`SecurityConfiguration` extends `WebSecurityConfigurerAdapter`),
  custom `UserDetailsService`, BCrypt encoder, and `SecurityUtils`.
- `resources/Resources.java` — i18n `I18NProvider`; supported locales NL, en-GB, en-US. UI strings
  come from `ResourceBundleMessageSource`, not hardcoded.

### Tournament templates (the central extension point)
Each tournament is a `LeagueTemplateService` implementation under
`backend/data/service/leaguetemplates/<tournament>/`. The `Template` enum
(`backend/model/enums/Template.java`) maps each league to a Spring bean name, and beans are wired in
`LeagueTemplateConfig`. At runtime `LeagueServiceImpl` resolves the service via
`beanFactory.getBean(league.getTemplate().getTemplateServiceBeanName(), LeagueTemplateService.class)`.

A template service owns: seeding the league (`run` → an `*Initializer` that loads teams/games, often
from `src/main/resources/csv/`), rendering league settings, and **scoring** (`calculateScoreOfPrediction`,
`calculateScoresForUser`, `calculateTotalUserScores`). Shared football logic lives in
`FootballInitializer`, `FootballLeagueScoreHelper`, and `FootballSettingsRenderer`.

**When adding a new tournament template:** add a service package + `Initializer`, register the bean in
`LeagueTemplateConfig`, add the enum constant in `Template`, and review `AdminModel#updateGameScoresGlobally`
(it fetches games per `matchNumber` per template — see the note in `Template.java`).

### Navigation
Two layers. Vaadin `@Route` handles top-level pages (login, register, activateAccount, etc.).
Inside the app, `MainView` (the `layout` for authenticated routes) hosts modules driven by the
`State` enum (`ui/main/views/state/State.java`): LEAGUE → `LeagueModule`, PROFILE → `ProfileView`,
ADMIN → `AdminModule`. URL constants live in `State.StateUrlConstants`. League content
(matches, groups, chat, overview/charts, settings, users, stocks) lives under
`ui/main/views/league/singleleague/`. Real-time updates use `ui/main/broadcast`.

### Notable conventions
- Entity fields use snake_case names mirroring DB columns (e.g. `home_team`, `away_team_fk`); FK id
  columns are mapped read-only alongside the `@ManyToOne` association.
- The app forces the JVM and Hibernate timezone to UTC (`FantasyLeagueApplication.main`,
  `hibernate.jdbc.time_zone=UTC`); render times in the user's zone in the UI, store UTC.
- Logging is Log4j2 (Spring's default Logback starter is excluded in `build.gradle`).
- `FantasyLeagueApplicationRunner` seeds a default EURO 2024 league on startup if missing.
- Actuator endpoints require the `ENDPOINT_ADMIN` role; Spring Boot Admin client points at :8081.
