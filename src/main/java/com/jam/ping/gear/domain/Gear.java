package com.jam.ping.gear.domain;

import com.jam.ping.category.domain.Category;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String link;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_id", nullable = false)
    private Maker maker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 1000)
    private String memo;

    @Builder
    private Gear(
            String name,
            String link,
            String imageUrl,
            Category category,
            Maker maker,
            User createdBy,
            User updatedBy,
            String memo
    ) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
        this.category = category;
        this.maker = maker;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.memo = memo;
    }

    /**
     * 용품 기본 정보를 수정합니다.
     */
    public void update(
            String name,
            String link,
            String imageUrl,
            Category category,
            Maker maker,
            User updatedBy,
            String memo
    ) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
        this.category = category;
        this.maker = maker;
        this.updatedBy = updatedBy;
        this.memo = memo;
    }
}
