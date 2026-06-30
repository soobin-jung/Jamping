package com.jam.ping.api.campingfam.invitation.controller;

import com.jam.ping.api.campingfam.invitation.controller.request.CampingFamInviteRequest;
import com.jam.ping.api.campingfam.invitation.controller.response.CampingFamInvitationResponse;
import com.jam.ping.api.campingfam.invitation.service.CampingFamInvitationService;
import com.jam.ping.api.user.main.oauth.CustomOAuth2User;
import com.jam.ping.global.response.ApiRes;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camping-fams")
public class CampingFamInvitationController {

    private final CampingFamInvitationService invitationService;

    @GetMapping("/{campingFamId}/invitations")
    public ApiRes<List<CampingFamInvitationResponse>> getInvitations(
            @PathVariable Long campingFamId
    ) {
        List<CampingFamInvitationResponse> responses = invitationService.getInvitations(campingFamId)
                .stream()
                .map(CampingFamInvitationResponse::from)
                .toList();

        return new ApiRes<List<CampingFamInvitationResponse>>().successData(responses);
    }

    @PostMapping("/{campingFamId}/invitations")
    public ApiRes<Void> invite(
            @PathVariable Long campingFamId,
            @Valid @RequestBody CampingFamInviteRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        invitationService.invite(campingFamId, oauth2User.getUserId(), request.emails());
        return new ApiRes<Void>().responseMsg("초대가 완료되었습니다.");
    }

    @PostMapping("/invitations/{token}/accept")
    public ApiRes<Void> accept(
            @PathVariable String token,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        invitationService.accept(token, oauth2User.getUserId());
        return new ApiRes<Void>().responseMsg("캠핑팸에 참여했습니다.");
    }

    @PostMapping("/invitations/{token}/reject")
    public ApiRes<Void> reject(@PathVariable String token) {
        invitationService.reject(token);
        return new ApiRes<Void>().responseMsg("초대를 거절했습니다.");
    }
}
