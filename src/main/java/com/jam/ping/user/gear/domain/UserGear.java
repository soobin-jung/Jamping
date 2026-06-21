package com.jam.ping.user.gear.domain;

import com.jam.ping.category.domain.Category;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.user.main.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(name = "user_gears")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGear {

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private UserGear(User user, Category category, Maker maker, Gear gear, String name, String memo) {
        this.user = user;
        this.category = category;
        this.maker = maker;
        this.gear = gear;
        this.name = name;
        this.memo = memo;
    }

    public void update(Category category, Maker maker, Gear gear, String name, String memo) {
        this.category = category;
        this.maker = maker;
        this.gear = gear;
        this.name = name;
        this.memo = memo;
    }
}
