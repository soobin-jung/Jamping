package com.jam.ping.api.campingfam.invitation.controller.response;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.invitation.dto.CampingFamInvitationDto;
import java.time.LocalDateTime;

public record CampingFamInvitationResponse(
        Long id,
        Long campingFamId,
        String email,
        InvitationStatus status,
        String token,
        LocalDateTime expiredAt,
        LocalDateTime createdAt
) {

    public static CampingFamInvitationResponse from(CampingFamInvitationDto dto) {
        return new CampingFamInvitationResponse(
                dto.id(),
                dto.campingFamId(),
                dto.email(),
                dto.status(),
                dto.token(),
                dto.expiredAt(),
                dto.createdAt()
        );
    }
}
