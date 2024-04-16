package com.jeno.fantasyleague.backend.data.service.repo.user;

import com.jeno.fantasyleague.backend.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.util.Set;

public class UserSpecifications {

	private UserSpecifications() {
	}

	/**
	 * SQL: WHERE user.username = <i>username</i>
	 * @param username
	 * @return
	 */
	public static Specification<User> usernameEquals(String username) {
		return (root, query, cb) ->
				cb.or(
						cb.equal(root.get("username"), username),
						cb.equal(root.get("email"), username)
				);
	}

	public static Specification<User> externalIdEquals(String externalId) {
		return (root, query, cb) -> cb.equal(root.get("externalAuthId"), externalId);
	}

	/**
	 * SQL: WHERE NOT user.id IN <i>idsToExclude</i>
	 * @param idsToExclude
	 * @return
	 */
	public static Specification<User> idNotIn(Set<Long> idsToExclude) {
		return (root, query, cb) -> cb.not(root.get("id").in(idsToExclude));
	}

	/**
	 * SQL: WHERE (user.name LIKE %<i>name</i>% OR user.username LIKE %<i>name</i>%)
	 * @param name
	 * @return
	 */
	public static Specification<User> usernameOrNameContains(String name) {
		return (root, query, cb) ->
			cb.or(
				cb.like(root.get("username"), "%" + name + "%"),
				cb.like(root.get("name"), "%" + name + "%"));
	}

	/**
	 * SQL: INNER JOIN FETCH user.roles
	 * @return
	 */
	public static Specification<User> rolesJoin() {
		return (root, query, cb) -> {
			root.fetch("roles", JoinType.LEFT);
			return cb.conjunction();
		};
	}

}
