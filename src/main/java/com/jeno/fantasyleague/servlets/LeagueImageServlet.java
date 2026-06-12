package com.jeno.fantasyleague.servlets;

import com.jeno.fantasyleague.backend.data.repository.LeagueRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LeagueImageServlet extends HttpServlet {

	private final LeagueRepository leagueRepository;

	public LeagueImageServlet(LeagueRepository leagueRepository) {
		this.leagueRepository = leagueRepository;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long leaguePk = ServletUtil.parseIdParameter(req, "leaguePk");
		byte[] picture = leaguePk != null ? leagueRepository.findLeaguePictureById(leaguePk) : null;
		if (picture == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		ServletUtil.writePng(resp, picture);
	}
}
