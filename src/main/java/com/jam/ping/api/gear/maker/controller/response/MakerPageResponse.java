package com.jam.ping.api.gear.maker.controller.response;

import com.jam.ping.api.gear.maker.domain.Maker;
import java.util.List;
import org.springframework.data.domain.Page;

public record MakerPageResponse(
        List<MakerResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        String keyword
) {

    /**
     * Page<Maker>를 관리자 목록 응답 형식으로 변환합니다.
     */
    public static MakerPageResponse from(Page<Maker> makerPage, String keyword) {
        return new MakerPageResponse(
                makerPage.getContent().stream()
                        .map(MakerResponse::from)
                        .toList(),
                makerPage.getNumber(),
                makerPage.getSize(),
                makerPage.getTotalElements(),
                makerPage.getTotalPages(),
                makerPage.hasNext(),
                makerPage.hasPrevious(),
                keyword
        );
    }
}
