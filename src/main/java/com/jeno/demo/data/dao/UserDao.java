package com.jeno.demo.data.dao;

import com.google.common.collect.Sets;
import com.jeno.demo.data.repository.RoleRepository;
import com.jeno.demo.data.repository.UserRepository;
import com.jeno.demo.data.security.CustomBCryptPasswordEncoder;
import com.jeno.demo.model.RoleName;
import com.jeno.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDao extends AbstractDao<User, UserRepository> {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CustomBCryptPasswordEncoder passwordEncoder;

	@Override
	protected void validateAdd(User ser, Map<String, String> errorMap) {
		if (repository.existsByEmail(ser.getEmail())) {
			errorMap.put("email", "Email has been used before");
		}
		if (repository.findByUsername(ser.getUsername()).isPresent()) {
			errorMap.put("username", "Username has been used before");
		}

		super.validateAdd(ser, errorMap);
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
