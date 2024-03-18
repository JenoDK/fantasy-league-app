package com.jeno.fantasyleague.backend.data.dao;

import com.google.common.collect.Sets;
import com.jeno.fantasyleague.backend.data.repository.RoleRepository;
import com.jeno.fantasyleague.backend.data.repository.UserRepository;
import com.jeno.fantasyleague.backend.data.security.CustomBCryptPasswordEncoder;
import com.jeno.fantasyleague.backend.model.RoleName;
import com.jeno.fantasyleague.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class UserDao extends AbstractDao<User, UserRepository> {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CustomBCryptPasswordEncoder passwordEncoder;

	@Override
	protected void validateAdd(User user, Map<String, String> errorMap) {
		if (repository.existsByEmail(user.getEmail())) {
			errorMap.put("email", "Email has been used before");
		}
		if (repository.findByUsername(user.getUsername()).isPresent()) {
			errorMap.put("username", "Username has been used before");
		}

		super.validateAdd(user, errorMap);
	}

	@Override
	protected void validateUpdate(User user, Map<String, String> errorMap) {
		Optional<User> emailExistsUser = repository.findByEmail(user.getEmail());
		if (emailExistsUser.isPresent() && !emailExistsUser.get().getId().equals(user.getId())) {
			errorMap.put("email", "Email has been used before");
		}
		Optional<User> usernameExistsUser = repository.findByUsername(user.getUsername());
		if (usernameExistsUser.isPresent() && !usernameExistsUser.get().getId().equals(user.getId())) {
			errorMap.put("username", "Username has been used before");
		}

		super.validateUpdate(user, errorMap);
	}

	@Override
	protected void preAdd(User user, Map<String, String> errorMap) {
		encodePasswordIfNeeded(user);
		user.setRoles(Sets.newHashSet(roleRepository.findByName(RoleName.ROLE_USER).get()));

		super.preAdd(user, errorMap);
	}

	@Override
	protected void preUpdate(User user, Map<String, String> errorMap) {
		encodePasswordIfNeeded(user);

		super.preUpdate(user, errorMap);
	}

	private void encodePasswordIfNeeded(User user) {
		String plainPassword = user.getPassword();
		if (!passwordEncoder.isBCrypt(plainPassword)) {
			String encryptedPassword = passwordEncoder.encode(plainPassword);
			user.setPassword(encryptedPassword);
		}
	}

}
