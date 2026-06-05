package com.jam.ping.maker.controller.response;

import com.jam.ping.maker.domain.Maker;
import java.time.LocalDateTime;

public record MakerResponse(
        Long id,
        String name,
        String nameEng,
        String homepageUrl,
        LocalDateTime createdAt,
        String createdBy,
        String updatedBy,
        LocalDateTime updatedAt
) {

    /**
     * Maker 엔티티를 관리자 응답 형식으로 변환합니다.
     */
    public static MakerResponse from(Maker maker) {
        return new MakerResponse(
                maker.getId(),
                maker.getName(),
                maker.getNameEng(),
                maker.getHomepageUrl(),
                maker.getCreatedAt(),
                maker.getCreatedBy() == null ? null : maker.getCreatedBy().getNickname(),
                maker.getUpdatedBy() == null ? null : maker.getUpdatedBy().getNickname(),
                maker.getUpdatedAt()
        );
    }
}
