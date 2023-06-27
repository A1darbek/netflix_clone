package com.ayderbek.netflxSpring.netflix_clone.repos;

import com.ayderbek.netflxSpring.netflix_clone.domain.Subscription;
import com.ayderbek.netflxSpring.netflix_clone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Subscription findByUser(User user);

    Subscription findByUserAndIsActive(User user, Boolean isActive);

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.isActive = true")
    Subscription findActiveByUserId(@Param("userId") Long userId);
}
