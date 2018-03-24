package com.jeno.fantasyleague.data.security;

import com.jeno.fantasyleague.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
			.map(user -> {
				if (user.isActive()) {
					return new CustomUserDetails(user);
				} else {
					throw new BadCredentialsException("Please activate your account");
				}
			})
			.orElseThrow(() -> new BadCredentialsException("Unknown username"));
	}

}
