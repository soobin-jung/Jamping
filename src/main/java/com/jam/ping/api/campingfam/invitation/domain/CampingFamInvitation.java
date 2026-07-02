package com.jam.ping.api.campingfam.invitation.domain;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.global.domain.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampingFamInvitation extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFam campingFam;

    @ManyToOne(fetch = FetchType.LAZY)
    private CampingFamMember inviter;

    @Column(nullable = false, length = 100)
    private String email;

    // 기존 유저면 연결, 비유저면 null
    @ManyToOne(fetch = FetchType.LAZY)
    private User invitedUser;

    // 이메일 링크 클릭 시 수락용 UUID 토큰
    @Column(nullable = false, length = 100, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public static CampingFamInvitation create(CampingFam campingFam, CampingFamMember inviter, String email, User invitedUser, String token, LocalDateTime expiredAt) {
        CampingFamInvitation invitation = new CampingFamInvitation();
        invitation.campingFam = campingFam;
        invitation.inviter = inviter;
        invitation.email = email;
        invitation.invitedUser = invitedUser;
        invitation.token = token;
        invitation.expiredAt = expiredAt;
        return invitation;
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }

    public void reject() {
        this.status = InvitationStatus.REJECTED;
    }

    public void expire() {
        this.status = InvitationStatus.EXPIRED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
