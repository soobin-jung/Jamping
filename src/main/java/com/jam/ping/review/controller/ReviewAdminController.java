package com.jam.ping.review.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.global.security.AuthUtils;
import com.jam.ping.review.code.ReviewStatus;
import com.jam.ping.review.controller.request.ReviewModerationRequest;
import com.jam.ping.review.controller.response.AdminReviewPageResponse;
import com.jam.ping.review.controller.response.AdminReviewResponse;
import com.jam.ping.review.domain.Review;
import com.jam.ping.review.service.ReviewService;
import com.jam.ping.user.main.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) ReviewStatus status
    ) {
        return new ApiRes<AdminReviewPageResponse>()
                .successData(AdminReviewPageResponse.from(
                        reviewService.getAllReviewsForAdmin(gearId, page, size, sort, direction, status)
                ));
    }

    @PutMapping("/{reviewId}/deactivate")
    public ApiRes<AdminReviewResponse> deactivateReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewModerationRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Review review = reviewService.deactivateReview(
                gearId,
                reviewId,
                request.moderationReason(),
                AuthUtils.getActorUserId(oauth2User)
        );

        return new ApiRes<AdminReviewResponse>()
                .successData(AdminReviewResponse.from(review))
                .manipulationOne(review.getId())
                .responseMsg("후기를 비활성화했습니다.");
    }

    @PutMapping("/{reviewId}/activate")
    public ApiRes<AdminReviewResponse> activateReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId
    ) {
        Review review = reviewService.activateReview(gearId, reviewId);

        return new ApiRes<AdminReviewResponse>()
                .successData(AdminReviewResponse.from(review))
                .manipulationOne(review.getId())
                .responseMsg("후기를 재활성화했습니다.");
    }
}
