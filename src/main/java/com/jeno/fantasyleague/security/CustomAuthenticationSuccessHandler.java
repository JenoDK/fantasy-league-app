package com.jeno.fantasyleague.security;

import com.jeno.fantasyleague.backend.data.dao.UserDao;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.service.repo.league.LeagueService;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeagueService leagueService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            String externalAuthId = getExternalAuthId(authentication);
            DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
            Optional<User> emailExistsUser = userRepository.findByExternalAuthId(externalAuthId);
            if (emailExistsUser.isEmpty()) {
                String oauthName = (String) oauthUser.getAttributes().get("name");

                User newUser = new User();
                newUser.setEmail((String) oauthUser.getAttributes().get("email"));
                newUser.setName(oauthName);
                newUser.setUsername(oauthName);
                newUser.setPassword(UUID.randomUUID().toString());
                newUser.setExternalAuthId(externalAuthId);
                User createdUser = userDao.add(newUser);
                leagueService.addUserToDefaultLeague(createdUser);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String getExternalAuthId(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String oauth2ClientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            if ("facebook".equals(oauth2ClientId)) {
                return oAuth2AuthenticationToken.getPrincipal().getAttribute("id");
            } else if ("google".equals(oauth2ClientId)) {
                return oAuth2AuthenticationToken.getPrincipal().getAttribute("sub");
            } else {
                return oAuth2AuthenticationToken.getPrincipal().getName();
            }
        }
        return null;
    }
}
