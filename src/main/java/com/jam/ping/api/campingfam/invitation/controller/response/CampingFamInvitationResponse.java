package com.jam.ping.api.campingfam.invitation.controller.response;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.invitation.domain.CampingFamInvitation;
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

    public static CampingFamInvitationResponse from(CampingFamInvitation invitation) {
        return new CampingFamInvitationResponse(
                invitation.getId(),
                invitation.getCampingFam().getId(),
                invitation.getEmail(),
                invitation.getStatus(),
                invitation.getToken(),
                invitation.getExpiredAt(),
                invitation.getCreatedAt()
        );
    }
}
