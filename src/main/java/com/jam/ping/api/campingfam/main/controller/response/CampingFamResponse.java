package com.jam.ping.api.campingfam.main.controller.response;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
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

    public static CampingFamResponse from(CampingFam campingFam) {
        return new CampingFamResponse(
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
