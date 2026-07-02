package com.jam.ping.api.campingfam.shopping.item.domain;

import com.jam.ping.api.campingfam.shopping.category.domain.CampingFamShoppingCategory;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamShoppingItem extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_fam_shopping_category_id", nullable = false)
    private CampingFamShoppingCategory category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String quantity;

    @Column(nullable = false)
    private boolean isPurchased = false;

    @Column(length = 200)
    private String memo;

    public static CampingFamShoppingItem create(CampingFamShoppingCategory category, String name, String quantity, String memo) {
        CampingFamShoppingItem item = new CampingFamShoppingItem();
        item.category = category;
        item.name = name;
        item.quantity = quantity;
        item.memo = memo;
        return item;
    }

    public void update(String name, String quantity, String memo) {
        this.name = name;
        this.quantity = quantity;
        this.memo = memo;
    }

    public void togglePurchased() {
        this.isPurchased = !this.isPurchased;
    }
}
