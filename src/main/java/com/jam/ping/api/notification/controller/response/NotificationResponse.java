package com.jam.ping.api.notification.controller.response;

import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.dto.NotificationDto;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        String message,
        Long referenceId,
        boolean isRead,
        LocalDateTime createdAt
) {

    public static NotificationResponse from(NotificationDto dto) {
        return new NotificationResponse(
                dto.id(),
                dto.type(),
                dto.message(),
                dto.referenceId(),
                dto.isRead(),
                dto.createdAt()
        );
    }
}
