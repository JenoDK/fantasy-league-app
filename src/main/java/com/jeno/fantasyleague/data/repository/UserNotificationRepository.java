package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import com.jeno.fantasyleague.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

	List<UserNotification> findByUserAndViewed(User user, Boolean viewed);

	@Query("SELECT n FROM UserNotification n " +
			"INNER JOIN FETCH n.user u " +
			"WHERE n.notification_type = :notificationType " +
			"AND n.reference_id = :referenceId")
	List<UserNotification> findByNotificationTypeAndReferenceIdAndJoinUsers(
			@Param("notificationType") NotificationType notificationType,
			@Param("referenceId") Long referenceId);

}
