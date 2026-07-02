package com.jam.ping.api.campingfam.settlement.result.domain;

import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.api.campingfam.settlement.expense.domain.CampingFamExpense;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamSettlement extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채권자(payer)는 expense.payer 로 접근
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_fam_expense_id", nullable = false)
    private CampingFamExpense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debtor_member_id", nullable = false)
    private CampingFamMember debtor;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal amount;

    public static CampingFamSettlement create(CampingFamExpense expense, CampingFamMember debtor, BigDecimal amount) {
        CampingFamSettlement settlement = new CampingFamSettlement();
        settlement.expense = expense;
        settlement.debtor = debtor;
        settlement.amount = amount;
        return settlement;
    }
}
