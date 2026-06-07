package com.jeno.fantasyleague.backend.data.repository;

import com.jeno.fantasyleague.backend.model.ReminderEmail;
import com.jeno.fantasyleague.backend.model.User;
import com.jeno.fantasyleague.backend.model.enums.ReminderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderEmailRepository extends JpaRepository<ReminderEmail, Long> {

	// Explicit JPQL: the underscored field names (reminder_type, reference_id) would otherwise be
	// misparsed by Spring Data's derived-query property traversal.
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReminderEmail r " +
			"WHERE r.user = :user AND r.reminder_type = :type AND r.reference_id = :referenceId")
	boolean existsForUserAndTypeAndReference(
			@Param("user") User user,
			@Param("type") ReminderType type,
			@Param("referenceId") Long referenceId);

	@Query("SELECT r FROM ReminderEmail r WHERE r.user = :user AND r.reminder_type = :type")
	List<ReminderEmail> findByUserAndType(@Param("user") User user, @Param("type") ReminderType type);
}
