package com.jam.ping.api.notification.dto;

import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.domain.Notification;
import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        NotificationType type,
        String message,
        Long referenceId,
        boolean isRead,
        LocalDateTime createdAt
) {

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getReferenceId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
