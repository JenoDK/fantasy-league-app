package com.jeno.fantasyleague.data.repository;

import com.jeno.fantasyleague.model.User;
import com.jeno.fantasyleague.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

	List<UserNotification> findByUserAndViewed(User user, Boolean viewed);

}
