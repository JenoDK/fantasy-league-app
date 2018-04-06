package com.jeno.fantasyleague.data.service.repo.user;

import com.jeno.fantasyleague.data.repository.UserRepository;
import com.jeno.fantasyleague.data.service.OffsetBasedPageRequest;
import com.jeno.fantasyleague.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> fetchUsersAndExclude(int offset, int limit, Set<Long> excludeIds) {
		return userRepository.findAll(UserSpecifications.idNotIn(excludeIds), getDefaultPageRequest(offset, limit)).getContent();
	}

	@Override
	public List<User> fetchUsersByNameAndExclude(String name, int offset, int limit, Set<Long> excludeIds) {
		return userRepository.findAll(
				UserSpecifications.usernameOrNameContains(name)
						.and(UserSpecifications.idNotIn(excludeIds)),
				getDefaultPageRequest(offset, limit)).getContent();
	}

	@Override
	public int getUserCountAndExclude(Set<Long> excludeIds) {
		return (int) userRepository.count(UserSpecifications.idNotIn(excludeIds));
	}

	@Override
	public int getUsersCountByNameAndExclude(String name, Set<Long> excludeIds) {
		return (int) userRepository.count(
				UserSpecifications.usernameOrNameContains(name)
						.and(UserSpecifications.idNotIn(excludeIds)));
	}

	@Override
	public Optional<User> findByUsernameAndJoinRoles(String name) {
		return userRepository.findOne(UserSpecifications.rolesJoin().and(UserSpecifications.usernameEquals(name)));
	}

	private static OffsetBasedPageRequest getDefaultPageRequest(int offset, int limit) {
		return new OffsetBasedPageRequest(offset, limit, Sort.Direction.ASC, "username", "name");
	}

}
