package com.jam.ping.review.domain;

import com.jam.ping.gear.domain.Gear;
import com.jam.ping.review.code.ReviewStatus;
import com.jam.ping.user.main.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_gear_user", columnNames = {"gear_id", "user_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gear_id", nullable = false)
    private Gear gear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReviewStatus status;

    @Column(length = 1000)
    private String moderationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderated_by")
    private User moderatedBy;

    private LocalDateTime moderatedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private Review(
            Gear gear,
            User user,
            Integer rating,
            String content,
            ReviewStatus status,
            String moderationReason,
            User moderatedBy,
            LocalDateTime moderatedAt
    ) {
        this.gear = gear;
        this.user = user;
        this.rating = rating;
        this.content = content;
        this.status = status;
        this.moderationReason = moderationReason;
        this.moderatedBy = moderatedBy;
        this.moderatedAt = moderatedAt;
    }

    public void update(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    public void deactivate(String moderationReason, User moderatedBy, LocalDateTime moderatedAt) {
        this.status = ReviewStatus.INACTIVE;
        this.moderationReason = moderationReason;
        this.moderatedBy = moderatedBy;
        this.moderatedAt = moderatedAt;
    }

    public void activate() {
        this.status = ReviewStatus.ACTIVE;
        this.moderationReason = null;
        this.moderatedBy = null;
        this.moderatedAt = null;
    }
}
