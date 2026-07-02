package com.jam.ping.api.notification.service;

import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.domain.Notification;
import com.jam.ping.api.notification.dto.NotificationDto;
import com.jam.ping.api.notification.repository.NotificationRepository;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.global.exception.ForbiddenException;
import com.jam.ping.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getMyNotifications(Long userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationDto::from)
                .toList();
    }

    public int getUnreadCount(Long userId) {
        return notificationRepository.findByReceiverIdAndIsReadFalse(userId).size();
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("알림을 찾을 수 없습니다."));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new ForbiddenException("권한이 없습니다.");
        }

        notification.read();
    }

    @Transactional
    public Notification createNotification(User receiver, NotificationType type, String message, Long referenceId) {
        return notificationRepository.save(Notification.create(receiver, type, message, referenceId));
    }
}
