package com.jeno.fantasyleague.backend.data.service.repo.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jeno.fantasyleague.backend.model.User;

public interface UserService {

	/**
	 * Fetch users and exclude certain id's, paged fetch with provided offset and limit
	 * @param offset
	 * @param limit
	 * @param excludeIds
	 * @return paged user fetch
	 */
	List<User> fetchUsersAndExclude(int offset, int limit, Set<Long> excludeIds);

	/**
	 * Fetch users containing <i>'%name%'</i> and exclude certain id's, paged fetch with provided offset and limit
	 * @param name
	 * @param offset
	 * @param limit
	 * @param excludeIds
	 * @return filtered paged user fetch
	 */
	List<User> fetchUsersByNameAndExclude(String name, int offset, int limit, Set<Long> excludeIds);

	/**
	 * Count method for {@link UserService#fetchUsersAndExclude(int, int, Set)}
	 */
	int getUserCountAndExclude(Set<Long> excludeIds);

	/**
	 * Count method for {@link UserService#fetchUsersByNameAndExclude(String, int, int, Set)}
	 */
	int getUsersCountByNameAndExclude(String filter, Set<Long> excludeIds);

	/**
	 * Fetches users where username equals <i>name</i> and join the users' roles
	 * @param name
	 * @return users with their roles
	 */
	Optional<User> findByUsernameAndJoinRoles(String name);
}
