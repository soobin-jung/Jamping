package com.jam.ping.api.user.gear.controller.response;

import com.jam.ping.api.user.gear.dto.UserGearDto;
import java.time.LocalDateTime;

public record UserGearResponse(
        Long id,
        Long userId,
        String userNickname,
        Long categoryId,
        String categoryName,
        Long makerId,
        String makerName,
        Long gearId,
        String gearName,
        String name,
        String memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserGearResponse from(UserGearDto dto) {
        return new UserGearResponse(
                dto.id(),
                dto.userId(),
                dto.userNickname(),
                dto.categoryId(),
                dto.categoryName(),
                dto.makerId(),
                dto.makerName(),
                dto.gearId(),
                dto.gearName(),
                dto.name(),
                dto.memo(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
