package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.Role;
import com.jeno.fantasyleague.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);

}