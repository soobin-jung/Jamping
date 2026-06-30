package com.jam.ping.api.campingfam.supply.assignment.repository;

import com.jam.ping.api.campingfam.supply.assignment.domain.CampingFamSupplyAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamSupplyAssignmentRepository extends JpaRepository<CampingFamSupplyAssignment, Long> {

    List<CampingFamSupplyAssignment> findBySupplyId(Long supplyId);

    Optional<CampingFamSupplyAssignment> findBySupplyIdAndMemberId(Long supplyId, Long memberId);

    void deleteBySupplyId(Long supplyId);
}
