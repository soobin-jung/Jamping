package com.jam.ping.api.gear.maker.controller.response;

import com.jam.ping.api.gear.maker.dto.MakerDto;
import java.time.LocalDateTime;

public record MakerResponse(
        Long id,
        String name,
        String nameEng,
        String homepageUrl,
        LocalDateTime createdAt,
        Long createdById,
        Long updatedById,
        LocalDateTime updatedAt
) {

    public static MakerResponse from(MakerDto dto) {
        return new MakerResponse(
                dto.id(),
                dto.name(),
                dto.nameEng(),
                dto.homepageUrl(),
                dto.createdAt(),
                dto.createdById(),
                dto.updatedById(),
                dto.updatedAt()
        );
    }
}
