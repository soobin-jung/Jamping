package com.jam.ping.api.campingfam.invitation.service;

import com.jam.ping.api.campingfam.invitation.code.InvitationStatus;
import com.jam.ping.api.campingfam.invitation.domain.CampingFamInvitation;
import com.jam.ping.api.campingfam.invitation.dto.CampingFamInvitationDto;
import com.jam.ping.api.campingfam.invitation.repository.CampingFamInvitationRepository;
import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.main.repository.CampingFamRepository;
import com.jam.ping.api.campingfam.member.code.CampingFamRole;
import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.api.campingfam.member.repository.CampingFamMemberRepository;
import com.jam.ping.api.notification.code.NotificationType;
import com.jam.ping.api.notification.service.NotificationService;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.api.user.main.repository.UserRepository;
import com.jam.ping.global.email.EmailService;
import com.jam.ping.global.exception.BadRequestException;
import com.jam.ping.global.exception.ConflictException;
import com.jam.ping.global.exception.ForbiddenException;
import com.jam.ping.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampingFamInvitationService {

    private final CampingFamInvitationRepository invitationRepository;
    private final CampingFamRepository campingFamRepository;
    private final CampingFamMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public List<CampingFamInvitationDto> getInvitations(Long campingFamId) {
        return invitationRepository.findByCampingFamId(campingFamId).stream()
                .map(CampingFamInvitationDto::from)
                .toList();
    }

    @Transactional
    public void invite(Long campingFamId, Long inviterUserId, List<String> emails) {
        CampingFam campingFam = findCampingFam(campingFamId);
        CampingFamMember inviter = memberRepository.findByCampingFamIdAndUserId(campingFamId, inviterUserId)
                .orElseThrow(() -> new ForbiddenException("캠핑팸 멤버가 아닙니다."));

        for (String email : emails) {
            if (invitationRepository.existsByCampingFamIdAndEmailAndStatus(campingFamId, email, InvitationStatus.PENDING)) {
                continue;
            }

            Optional<User> targetUser = userRepository.findByEmail(email);
            String token = UUID.randomUUID().toString();

            CampingFamInvitation invitation = CampingFamInvitation.create(
                    campingFam, inviter, email, targetUser.orElse(null), token, LocalDateTime.now().plusDays(7)
            );
            invitationRepository.save(invitation);

            if (targetUser.isPresent()) {
                String message = String.format("[%s] 캠핑팸에 초대되었습니다.", campingFam.getName());
                notificationService.createNotification(
                        targetUser.get(), NotificationType.CAMPING_FAM_INVITE, message, invitation.getId()
                );
            } else {
                sendInviteEmail(email, token, campingFam.getName());
            }
        }
    }

    @Transactional
    public void accept(String token, Long userId) {
        CampingFamInvitation invitation = findByToken(token);

        if (invitation.isExpired()) {
            invitation.expire();
            throw new BadRequestException("만료된 초대입니다.");
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("이미 처리된 초대입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        Long campingFamId = invitation.getCampingFam().getId();
        if (memberRepository.existsByCampingFamIdAndUserId(campingFamId, userId)) {
            throw new ConflictException("이미 캠핑팸 멤버입니다.");
        }

        memberRepository.save(CampingFamMember.create(invitation.getCampingFam(), user, CampingFamRole.GUEST));
        invitation.accept();
    }

    @Transactional
    public void reject(String token) {
        CampingFamInvitation invitation = findByToken(token);

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("이미 처리된 초대입니다.");
        }

        invitation.reject();
    }

    private CampingFam findCampingFam(Long id) {
        return campingFamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("캠핑팸을 찾을 수 없습니다."));
    }

    private CampingFamInvitation findByToken(String token) {
        return invitationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("초대를 찾을 수 없습니다."));
    }

    private void sendInviteEmail(String email, String token, String campingFamName) {
        try {
            emailService.sendCampingFamInvitation(email, campingFamName, token);
        } catch (Exception e) {
            log.error("[초대 이메일 발송 실패] to={}, error={}", email, e.getMessage());
        }
    }
}
