package com.jam.ping.api.gear.main.controller.response;

import com.jam.ping.api.gear.main.dto.GearDto;
import java.util.List;
import org.springframework.data.domain.Page;

public record GearPageResponse(
        List<GearResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        Long categoryId,
        Long makerId,
        String keyword
) {

    public static GearPageResponse from(Page<GearDto> gearPage, Long categoryId, Long makerId, String keyword) {
        return new GearPageResponse(
                gearPage.getContent().stream()
                        .map(GearResponse::from)
                        .toList(),
                gearPage.getNumber(),
                gearPage.getSize(),
                gearPage.getTotalElements(),
                gearPage.getTotalPages(),
                gearPage.hasNext(),
                gearPage.hasPrevious(),
                categoryId,
                makerId,
                keyword
        );
    }
}
