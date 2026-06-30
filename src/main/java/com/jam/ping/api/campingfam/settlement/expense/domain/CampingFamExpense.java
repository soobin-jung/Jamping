package com.jam.ping.api.campingfam.settlement.expense.domain;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamExpense extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFamMember payer;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal amount;

    @Column(nullable = false, length = 100)
    private String itemName;
    
    // 추후에 s3 연동되면 영수증 사진 경로도 고려

    @Builder
    private CampingFamExpense(CampingFam campingFam, CampingFamMember payer, BigDecimal amount, String itemName) {
        this.campingFam = campingFam;
        this.payer = payer;
        this.amount = amount;
        this.itemName = itemName;
    }

    public void update(BigDecimal amount, String itemName) {
        this.amount = amount;
        this.itemName = itemName;
    }
}
