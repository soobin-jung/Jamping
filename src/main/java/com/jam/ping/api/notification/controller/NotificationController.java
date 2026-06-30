package com.jam.ping.api.notification.controller;

import com.jam.ping.api.notification.controller.response.NotificationResponse;
import com.jam.ping.api.notification.service.NotificationService;
import com.jam.ping.api.user.main.oauth.CustomOAuth2User;
import com.jam.ping.global.response.ApiRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiRes<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        List<NotificationResponse> responses = notificationService.getMyNotifications(oauth2User.getUserId())
                .stream()
                .map(NotificationResponse::from)
                .toList();

        return new ApiRes<List<NotificationResponse>>().successData(responses);
    }

    @GetMapping("/unread-count")
    public ApiRes<Integer> getUnreadCount(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        return new ApiRes<Integer>().successData(
                notificationService.getUnreadCount(oauth2User.getUserId())
        );
    }

    @PatchMapping("/{id}/read")
    public ApiRes<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        notificationService.markAsRead(id, oauth2User.getUserId());
        return new ApiRes<Void>()
                .manipulationOne(id)
                .responseMsg("읽음 처리되었습니다.");
    }
}
