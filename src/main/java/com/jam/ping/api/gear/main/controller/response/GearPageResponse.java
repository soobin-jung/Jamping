package com.jam.ping.api.gear.main.controller.response;

import com.jam.ping.api.gear.main.domain.Gear;
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

    /**
     * Page<Gear>를 관리자 목록 응답 형식으로 변환합니다.
     */
    public static GearPageResponse from(Page<Gear> gearPage, Long categoryId, Long makerId, String keyword) {
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
