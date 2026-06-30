package com.jam.ping.api.campingfam.member.repository;

import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamMemberRepository extends JpaRepository<CampingFamMember, Long> {

    List<CampingFamMember> findByCampingFamId(Long campingFamId);

    Optional<CampingFamMember> findByCampingFamIdAndUserId(Long campingFamId, Long userId);

    boolean existsByCampingFamIdAndUserId(Long campingFamId, Long userId);
}
