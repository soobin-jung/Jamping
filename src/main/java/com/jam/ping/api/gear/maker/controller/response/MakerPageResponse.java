package com.jam.ping.api.gear.maker.controller.response;

import com.jam.ping.api.gear.maker.dto.MakerDto;
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

    public static MakerPageResponse from(Page<MakerDto> makerPage, String keyword) {
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
