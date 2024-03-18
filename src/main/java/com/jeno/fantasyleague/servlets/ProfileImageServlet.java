package com.jeno.fantasyleague.servlets;

import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProfileImageServlet extends HttpServlet {

	private static final Logger LOG = LogManager.getLogger(ProfileImageServlet.class.getName());

	private final UserRepository userRepository;

	public ProfileImageServlet(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("image/png");
		String userPk = req.getParameter("userPk");
		if (userPk != null) {
			Optional<User> userOpt = userRepository.findById(Long.valueOf(userPk));
			if (userOpt.isPresent() && userOpt.get().getProfile_picture() != null) {
				try {
					resp.getOutputStream().write(userOpt.get().getProfile_picture());
				} catch (IOException e) {
					LOG.warn("Error returning profile picture for " + userPk, e);
				}
			}
		}
	}
}
