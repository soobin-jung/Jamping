package com.jam.ping.api.campingfam.supply.main.domain;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamSupply extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @Column(nullable = false, length = 100)
    private String name;

    public static CampingFamSupply create(CampingFam campingFam, String name) {
        CampingFamSupply supply = new CampingFamSupply();
        supply.campingFam = campingFam;
        supply.name = name;
        return supply;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
