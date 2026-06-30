package com.jam.ping.api.notification.domain;

import com.jam.ping.api.notification.code.NotificationType;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(nullable = false, length = 300)
    private String message;

    // 알림 대상 엔티티의 ID (ex. CampingFamInvitation.id)
    @Column(nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    private boolean isRead = false;

    @Builder
    private Notification(User receiver, NotificationType type, String message, Long referenceId) {
        this.receiver = receiver;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
    }

    public void read() {
        this.isRead = true;
    }
}
