package com.jam.ping.api.notification.controller.response;

import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.domain.Notification;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        String message,
        Long referenceId,
        boolean isRead,
        LocalDateTime createdAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getReferenceId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
