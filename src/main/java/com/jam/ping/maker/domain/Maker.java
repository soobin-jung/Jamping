package com.jam.ping.maker.domain;

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
public class Maker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "name_eng", length = 100)
    private String nameEng;

    @Column(length = 500)
    private String homepageUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private Maker(
            String name,
            String nameEng,
            String homepageUrl,
            User createdBy,
            User updatedBy
    ) {
        this.name = name;
        this.nameEng = nameEng;
        this.homepageUrl = homepageUrl;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    /**
     * 메이커의 기본 관리 정보를 수정합니다.
     */
    public void update(String name, String nameEng, String homepageUrl, User updatedBy) {
        this.name = name;
        this.nameEng = nameEng;
        this.homepageUrl = homepageUrl;
        this.updatedBy = updatedBy;
    }
}
