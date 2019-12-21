package com.jeno.fantasyleague.security;

import com.jeno.fantasyleague.backend.data.security.CustomUserDetails;
import com.jeno.fantasyleague.backend.data.service.repo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userService.findByUsernameAndJoinRoles(username)
			.map(user -> {
				if (user.isActive()) {
					return new CustomUserDetails(user);
				} else {
					throw new BadCredentialsException("Please activate your account");
				}
			})
			.orElseThrow(() -> new UsernameNotFoundException("Unknown username"));
	}

}
