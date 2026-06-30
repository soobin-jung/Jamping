package com.jam.ping.api.notification.service;

import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.domain.Notification;
import com.jam.ping.api.notification.repository.NotificationRepository;
import com.jam.ping.api.user.main.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
    }

    public int getUnreadCount(Long userId) {
        return notificationRepository.findByReceiverIdAndIsReadFalse(userId).size();
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        notification.read();
    }

    @Transactional
    public Notification createNotification(User receiver, NotificationType type, String message, Long referenceId) {
        return notificationRepository.save(
                Notification.builder()
                        .receiver(receiver)
                        .type(type)
                        .message(message)
                        .referenceId(referenceId)
                        .build()
        );
    }
}
