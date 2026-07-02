package com.jam.ping.api.user.gear.domain;

import com.jam.ping.api.gear.category.domain.Category;
import com.jam.ping.api.gear.main.domain.Gear;
import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.api.gear.maker.domain.Maker;
import com.jam.ping.api.user.main.domain.User;
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
public class UserGear extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id")
    private Maker maker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gear_id")
    private Gear gear;

    @Column(length = 100)
    private String name;

    @Column(length = 1000)
    private String memo;

    public static UserGear create(User user, Category category, Maker maker, Gear gear, String name, String memo) {
        UserGear userGear = new UserGear();
        userGear.user = user;
        userGear.category = category;
        userGear.maker = maker;
        userGear.gear = gear;
        userGear.name = name;
        userGear.memo = memo;
        return userGear;
    }

    public void update(Category category, Maker maker, Gear gear, String name, String memo) {
        this.category = category;
        this.maker = maker;
        this.gear = gear;
        this.name = name;
        this.memo = memo;
    }
}
