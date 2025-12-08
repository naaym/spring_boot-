package com.delta_nutritionMVC.delta.admin.repositories;

import com.delta_nutritionMVC.delta.admin.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByReadFlagFalse();

    List<Notification> findAllByReadFlagFalseOrderByCreatedAtDesc();

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findAllByOrderIdAndReadFlagFalse(Long orderId);
}
