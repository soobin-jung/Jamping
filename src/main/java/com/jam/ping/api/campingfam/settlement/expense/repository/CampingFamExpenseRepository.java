package com.jam.ping.api.campingfam.settlement.expense.repository;

import com.jam.ping.api.campingfam.settlement.expense.domain.CampingFamExpense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamExpenseRepository extends JpaRepository<CampingFamExpense, Long> {

    List<CampingFamExpense> findByCampingFamId(Long campingFamId);

    void deleteByCampingFamId(Long campingFamId);
}
