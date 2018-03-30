package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.ContestantGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantGroupRepository extends JpaRepository<ContestantGroup, Long> {
}
