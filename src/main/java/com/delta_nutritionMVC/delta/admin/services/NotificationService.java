package com.delta_nutritionMVC.delta.admin.services;

import com.delta_nutritionMVC.delta.admin.models.Notification;
import com.delta_nutritionMVC.delta.admin.repositories.NotificationRepository;
import com.delta_nutritionMVC.delta.landing.models.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification notifyNewOrder(Order order) {
        Notification notification = new Notification(order,
                String.format("Nouvelle commande #%d par %s", order.getId(), order.getFullName()));
        return notificationRepository.save(notification);
    }

    public List<Notification> fetchUnreadNotifications() {
        return notificationRepository.findAllByReadFlagFalseOrderByCreatedAtDesc();
    }

    public long countUnread() {
        return notificationRepository.countByReadFlagFalse();
    }

    public List<Notification> fetchAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Optional<Notification> markAsRead(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.markAsRead();
            return notification;
        });
    }

    @Transactional
    public void markOrderNotificationsAsRead(Long orderId) {
        notificationRepository.findAllByOrderIdAndReadFlagFalse(orderId)
                .forEach(Notification::markAsRead);
    }
}
