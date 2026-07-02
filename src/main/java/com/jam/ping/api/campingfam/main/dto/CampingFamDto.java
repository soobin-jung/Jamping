package com.jam.ping.api.campingfam.main.dto;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CampingFamDto(
        Long id,
        String name,
        Long campSiteId,
        String reservationSites,
        LocalDate campingStartDate,
        LocalDate campingEndDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static CampingFamDto from(CampingFam campingFam) {
        return new CampingFamDto(
                campingFam.getId(),
                campingFam.getName(),
                campingFam.getCampSite().getId(),
                campingFam.getReservationSites(),
                campingFam.getCampingStartDate(),
                campingFam.getCampingEndDate(),
                campingFam.getCreatedAt(),
                campingFam.getUpdatedAt()
        );
    }
}
