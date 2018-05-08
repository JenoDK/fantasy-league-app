package com.jeno.fantasyleague.data.service.leaguetemplates.custom;

import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueSettingRenderer;
import com.jeno.fantasyleague.data.service.leaguetemplates.LeagueTemplateService;
import com.jeno.fantasyleague.data.service.leaguetemplates.TemplateException;
import com.jeno.fantasyleague.model.League;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomTemplateService implements LeagueTemplateService {

	@Autowired
	private CustomSettingRenderer customSettingRenderer;

	@Override
	public void run(League newLeague, User user) throws TemplateException {

	}

	@Override
	public LeagueSettingRenderer getLeagueSettingRenderer() {
		return customSettingRenderer;
	}

}
