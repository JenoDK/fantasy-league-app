package com.jeno.wkapp.data.dao;

import com.google.common.collect.Sets;
import com.jeno.wkapp.data.repository.RoleRepository;
import com.jeno.wkapp.data.repository.UserRepository;
import com.jeno.wkapp.model.RoleName;
import com.jeno.wkapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserDao extends AbstractDao<User, UserRepository> {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	protected Map<String, String> validate(User object, Map<String, String> errorMap) {
		if (repository.existsByEmail(object.getEmail())) {
			errorMap.put("email", "Email has been used before");
		}
		if (repository.findByUsername(object.getUsername()).isPresent()) {
			errorMap.put("username", "Username has been used before");
		}
		return super.validate(object, errorMap);
	}

	@Override
	protected void preAdd(User user) {
		String plainPassword = user.getPassword();
		String encryptedPassword = passwordEncoder.encode(plainPassword);
		user.setPassword(encryptedPassword);

		user.setRoles(Sets.newHashSet(roleRepository.findByName(RoleName.ROLE_USER).get()));
	}

}
