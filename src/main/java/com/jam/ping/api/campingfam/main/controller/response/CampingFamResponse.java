package com.jam.ping.api.campingfam.main.controller.response;

import com.jam.ping.api.campingfam.main.dto.CampingFamDto;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CampingFamResponse(
        Long id,
        String name,
        Long campSiteId,
        String reservationSites,
        LocalDate campingStartDate,
        LocalDate campingEndDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static CampingFamResponse from(CampingFamDto dto) {
        return new CampingFamResponse(
                dto.id(),
                dto.name(),
                dto.campSiteId(),
                dto.reservationSites(),
                dto.campingStartDate(),
                dto.campingEndDate(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }
}
