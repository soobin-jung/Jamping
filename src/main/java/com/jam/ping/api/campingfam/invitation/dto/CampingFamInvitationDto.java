package com.jam.ping.api.campingfam.invitation.dto;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.invitation.domain.CampingFamInvitation;
import java.time.LocalDateTime;

public record CampingFamInvitationDto(
        Long id,
        Long campingFamId,
        String email,
        InvitationStatus status,
        String token,
        LocalDateTime expiredAt,
        LocalDateTime createdAt
) {

    public static CampingFamInvitationDto from(CampingFamInvitation invitation) {
        return new CampingFamInvitationDto(
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
