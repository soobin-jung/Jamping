package com.jam.ping.api.gear.review.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.global.security.AuthUtils;
import com.jam.ping.api.gear.review.controller.request.AdminReviewSearchRequest;
import com.jam.ping.api.gear.review.controller.request.ReviewModerationRequest;
import com.jam.ping.api.gear.review.controller.response.AdminReviewPageResponse;
import com.jam.ping.api.gear.review.controller.response.AdminReviewResponse;
import com.jam.ping.api.gear.review.dto.ReviewDto;
import com.jam.ping.api.gear.review.service.ReviewService;
import com.jam.ping.api.user.main.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/gears/{gearId}/reviews")
public class ReviewAdminController {

    private final ReviewService reviewService;

    @GetMapping
    public ApiRes<AdminReviewPageResponse> getReviews(
            @PathVariable Long gearId,
            @Valid @ModelAttribute AdminReviewSearchRequest request
    ) {
        return new ApiRes<AdminReviewPageResponse>()
                .successData(AdminReviewPageResponse.from(
                        reviewService.getAllReviewsForAdmin(gearId, request.page(), request.size(), request.sort(), request.direction(), request.status())
                ));
    }

    @PutMapping("/{reviewId}/deactivate")
    public ApiRes<AdminReviewResponse> deactivateReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewModerationRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        ReviewDto review = reviewService.deactivateReview(
                gearId,
                reviewId,
                request.moderationReason(),
                AuthUtils.getActorUserId(oauth2User)
        );

        return new ApiRes<AdminReviewResponse>()
                .successData(AdminReviewResponse.from(review))
                .manipulationOne(review.id())
                .responseMsg("후기를 비활성화했습니다.");
    }

    @PutMapping("/{reviewId}/activate")
    public ApiRes<AdminReviewResponse> activateReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId
    ) {
        ReviewDto review = reviewService.activateReview(gearId, reviewId);

        return new ApiRes<AdminReviewResponse>()
                .successData(AdminReviewResponse.from(review))
                .manipulationOne(review.id())
                .responseMsg("후기를 재활성화했습니다.");
    }
}
