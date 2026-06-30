package com.jam.ping.api.campingfam.invitation.repository;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.invitation.domain.CampingFamInvitation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamInvitationRepository extends JpaRepository<CampingFamInvitation, Long> {

    Optional<CampingFamInvitation> findByToken(String token);

    List<CampingFamInvitation> findByCampingFamId(Long campingFamId);

    boolean existsByCampingFamIdAndEmailAndStatus(Long campingFamId, String email, InvitationStatus status);
}
