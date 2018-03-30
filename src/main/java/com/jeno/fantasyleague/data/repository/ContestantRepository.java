package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {
}
