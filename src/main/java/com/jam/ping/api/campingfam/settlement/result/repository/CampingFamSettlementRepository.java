package com.jam.ping.api.campingfam.settlement.result.repository;

import com.jam.ping.api.campingfam.settlement.result.domain.CampingFamSettlement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamSettlementRepository extends JpaRepository<CampingFamSettlement, Long> {

    List<CampingFamSettlement> findByExpenseId(Long expenseId);

    List<CampingFamSettlement> findByDebtorId(Long debtorId);

    void deleteByExpenseId(Long expenseId);
}
