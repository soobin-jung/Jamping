package com.jam.ping.api.gear.maker.dto;

import com.jam.ping.api.gear.maker.domain.Maker;
import java.time.LocalDateTime;

public record MakerDto(
        Long id,
        String name,
        String nameEng,
        String homepageUrl,
        LocalDateTime createdAt,
        Long createdById,
        Long updatedById,
        LocalDateTime updatedAt
) {

    public static MakerDto from(Maker maker) {
        return new MakerDto(
                maker.getId(),
                maker.getName(),
                maker.getNameEng(),
                maker.getHomepageUrl(),
                maker.getCreatedAt(),
                maker.getCreatedById(),
                maker.getUpdatedById(),
                maker.getUpdatedAt()
        );
    }
}
