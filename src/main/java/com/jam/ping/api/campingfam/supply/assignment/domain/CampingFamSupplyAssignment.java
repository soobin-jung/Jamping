package com.jam.ping.api.campingfam.supply.assignment.domain;

import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.api.campingfam.supply.main.domain.CampingFamSupply;
import com.jam.ping.api.user.gear.domain.UserGear;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_supply_assignment_supply_member",
        columnNames = {"camping_fam_supply_id", "camping_fam_member_id"}
    )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamSupplyAssignment extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_fam_supply_id", nullable = false)
    private CampingFamSupply supply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_fam_member_id", nullable = false)
    private CampingFamMember member;

    // null이면 내 장비가 아닌 자유 입력
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_gear_id")
    private UserGear userGear;

    @Column(length = 200)
    private String memo;

    public static CampingFamSupplyAssignment create(CampingFamSupply supply, CampingFamMember member, UserGear userGear, String memo) {
        CampingFamSupplyAssignment assignment = new CampingFamSupplyAssignment();
        assignment.supply = supply;
        assignment.member = member;
        assignment.userGear = userGear;
        assignment.memo = memo;
        return assignment;
    }

    public void update(UserGear userGear, String memo) {
        this.userGear = userGear;
        this.memo = memo;
    }
}
