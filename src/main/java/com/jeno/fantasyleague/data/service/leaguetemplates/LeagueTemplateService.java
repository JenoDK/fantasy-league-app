package com.jeno.fantasyleague.data.service.leaguetemplates;

import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;

public interface LeagueTemplateService {

	void run(League newLeague, User user) throws TemplateException;

	LeagueSettingRenderer getLeagueSettingRenderer();

}
