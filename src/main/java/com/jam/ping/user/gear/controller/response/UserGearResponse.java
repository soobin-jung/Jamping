package com.jam.ping.user.gear.controller.response;

import com.jam.ping.user.gear.domain.UserGear;
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

    public static UserGearResponse from(UserGear userGear) {
        return new UserGearResponse(
                userGear.getId(),
                userGear.getUser().getId(),
                userGear.getUser().getNickname(),
                userGear.getCategory().getId(),
                userGear.getCategory().getName(),
                userGear.getMaker() == null ? null : userGear.getMaker().getId(),
                userGear.getMaker() == null ? null : userGear.getMaker().getName(),
                userGear.getGear() == null ? null : userGear.getGear().getId(),
                userGear.getGear() == null ? null : userGear.getGear().getName(),
                userGear.getName(),
                userGear.getMemo(),
                userGear.getCreatedAt(),
                userGear.getUpdatedAt()
        );
    }
}
