package com.jam.ping.campingfam.member.domain;

import com.jam.ping.campingfam.main.domain.CampingFam;
import com.jam.ping.campingfam.member.code.CampingFamRole;
import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.user.main.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_camping_fam_member_fam_user", columnNames = {"camping_fam_id", "user_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamMember extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CampingFamRole role;

    @Builder
    private CampingFamMember(CampingFam campingFam, User user, CampingFamRole role) {
        this.campingFam = campingFam;
        this.user = user;
        this.role = role;
    }
}
