package com.jam.ping.user.gear.controller.response;

import com.jam.ping.user.gear.domain.UserGear;
import java.util.List;
import org.springframework.data.domain.Page;

public record UserGearPageResponse(
        List<UserGearResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static UserGearPageResponse from(Page<UserGear> userGearPage) {
        return new UserGearPageResponse(
                userGearPage.getContent().stream()
                        .map(UserGearResponse::from)
                        .toList(),
                userGearPage.getNumber(),
                userGearPage.getSize(),
                userGearPage.getTotalElements(),
                userGearPage.getTotalPages(),
                userGearPage.hasNext(),
                userGearPage.hasPrevious()
        );
    }
}
