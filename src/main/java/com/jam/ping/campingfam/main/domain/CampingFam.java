package com.jam.ping.campingfam.main.domain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "camping_fams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "camp_site_id", nullable = false)
    private Long campSiteId;

    @Column(name = "reservation_sites", nullable = false, length = 255)
    private String reservationSites;

    @Column(name = "camping_start_date", nullable = false)
    private LocalDate campingStartDate;

    @Column(name = "camping_end_date", nullable = false)
    private LocalDate campingEndDate;

    @Builder
    private CampingFam(
            User createdBy,
            String name,
            Long campSiteId,
            String reservationSites,
            LocalDate campingStartDate,
            LocalDate campingEndDate
    ) {
        this.createdBy = createdBy;
        this.name = name;
        this.campSiteId = campSiteId;
        this.reservationSites = reservationSites;
        this.campingStartDate = campingStartDate;
        this.campingEndDate = campingEndDate;
    }
}
