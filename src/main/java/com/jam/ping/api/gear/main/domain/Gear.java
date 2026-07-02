package com.jam.ping.api.gear.main.domain;

import com.jam.ping.api.gear.category.domain.Category;
import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.api.gear.maker.domain.Maker;
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
public class Gear extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String link;

    @Column(length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id", nullable = false)
    private Maker maker;

    @Column(length = 1000)
    private String memo;

    public static Gear create(String name, String link, String imageUrl, Category category, Maker maker, String memo) {
        Gear gear = new Gear();
        gear.name = name;
        gear.link = link;
        gear.imageUrl = imageUrl;
        gear.category = category;
        gear.maker = maker;
        gear.memo = memo;
        return gear;
    }

    public void update(String name, String link, String imageUrl, Category category, Maker maker, String memo) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
        this.category = category;
        this.maker = maker;
        this.memo = memo;
    }
}
