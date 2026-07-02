package com.jam.ping.api.campingfam.shopping.category.domain;

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
public class CampingFamShoppingCategory extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @Column(nullable = false, length = 50)
    private String name;

    public static CampingFamShoppingCategory create(CampingFam campingFam, String name) {
        CampingFamShoppingCategory category = new CampingFamShoppingCategory();
        category.campingFam = campingFam;
        category.name = name;
        return category;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
