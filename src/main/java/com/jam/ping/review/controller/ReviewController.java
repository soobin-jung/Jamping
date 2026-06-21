package com.jam.ping.review.controller;

import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AuthUtils;
import com.jam.ping.review.controller.request.ReviewRequest;
import com.jam.ping.review.controller.response.ReviewPageResponse;
import com.jam.ping.review.controller.response.ReviewResponse;
import com.jam.ping.review.domain.Review;
import com.jam.ping.review.service.ReviewService;
import com.jam.ping.user.main.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gears/{gearId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ApiRes<ReviewPageResponse> getReviews(
            @PathVariable Long gearId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        return new ApiRes<ReviewPageResponse>()
                .successData(ReviewPageResponse.from(
                        reviewService.getActiveReviews(gearId, page, size),
                        AuthUtils.getActorUserId(oauth2User)
                ));
    }

    @PostMapping
    public ResponseEntity<ApiRes<ReviewResponse>> createReview(
            @PathVariable Long gearId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Review review = reviewService.createReview(
                gearId,
                request.rating(),
                request.content(),
                AuthUtils.getActorUserId(oauth2User)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<ReviewResponse>()
                        .successData(ReviewResponse.from(review, AuthUtils.getActorUserId(oauth2User)))
                        .manipulationOne(review.getId())
                        .responseMsg("후기를 등록했습니다."));
    }

    @PutMapping("/{reviewId}")
    public ApiRes<ReviewResponse> updateReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Review review = reviewService.updateReview(
                gearId,
                reviewId,
                request.rating(),
                request.content(),
                AuthUtils.getActorUserId(oauth2User)
        );

        return new ApiRes<ReviewResponse>()
                .successData(ReviewResponse.from(review, AuthUtils.getActorUserId(oauth2User)))
                .manipulationOne(review.getId())
                .responseMsg("후기를 수정했습니다.");
    }

    @DeleteMapping("/{reviewId}")
    public ApiRes<Void> deleteReview(
            @PathVariable Long gearId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        reviewService.deleteReview(gearId, reviewId, AuthUtils.getActorUserId(oauth2User));

        return new ApiRes<Void>()
                .manipulationOne(reviewId)
                .responseMsg("후기를 삭제했습니다.");
    }

}
