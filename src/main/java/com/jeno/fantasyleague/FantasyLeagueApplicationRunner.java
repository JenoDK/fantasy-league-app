package com.jeno.fantasyleague;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.League;
import com.jeno.fantasyleague.backend.model.enums.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FantasyLeagueApplicationRunner implements ApplicationRunner {

    public static final String DEFAULT_LUEAGUE_GUID = "47021ead-d83a-4a34-bd3a-38706a05a0e7";

    @Autowired
    private LeagueService leagueService;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        leagueRepository.findByGuid(DEFAULT_LUEAGUE_GUID);
        if (leagueRepository.findByGuid(DEFAULT_LUEAGUE_GUID).isEmpty()) {
            userRepository.findByEmail("jenodekeyzer@gmail.com").ifPresent(jenoUser -> {
                League defaultLeague = new League();
                defaultLeague.setName("EURO 2024 Pronostiek");
                defaultLeague.setGuid(DEFAULT_LUEAGUE_GUID);
                defaultLeague.setTemplate(Template.UEFA_EURO_2024);
                defaultLeague.getOwners().add(jenoUser);
                defaultLeague.setCreatedBy(jenoUser);
                leagueService.addLeague(defaultLeague, jenoUser);
            });
        }
    }
}
