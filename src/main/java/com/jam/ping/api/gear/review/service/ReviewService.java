package com.jam.ping.api.gear.review.service;

import com.jam.ping.api.gear.main.domain.Gear;
import com.jam.ping.api.gear.main.repository.GearRepository;
import com.jam.ping.api.gear.review.code.ReviewStatus;
import com.jam.ping.api.gear.review.domain.Review;
import com.jam.ping.api.gear.review.repository.ReviewRepository;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.api.user.main.service.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GearRepository gearRepository;
    private final UserService userService;

    public Page<Review> getActiveReviews(Long gearId, int page, int size) {
        findGear(gearId);
        return reviewRepository.findByGearIdAndStatus(gearId, ReviewStatus.ACTIVE, pageRequest(page, size));
    }

    public Page<Review> getAllReviewsForAdmin(Long gearId, int page, int size, String sort, String direction, ReviewStatus status) {
        findGear(gearId);
        Pageable pageable = adminPageRequest(page, size, sort, direction);
        if (status != null) {
            return reviewRepository.findByGearIdAndStatus(gearId, status, pageable);
        }
        return reviewRepository.findByGearId(gearId, pageable);
    }

    @Transactional
    public Review createReview(Long gearId, Integer rating, String content, Long actorUserId) {
        Gear gear = findGear(gearId);
        User actorUser = userService.getActorUser(actorUserId);

        if (reviewRepository.existsByGearIdAndUserId(gearId, actorUserId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 해당 장비에 후기를 작성했습니다.");
        }

        Review review = Review.builder()
                .gear(gear)
                .user(actorUser)
                .rating(rating)
                .content(content.trim())
                .status(ReviewStatus.ACTIVE)
                .build();

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long gearId, Long reviewId, Integer rating, String content, Long actorUserId) {
        Review review = findReview(gearId, reviewId);
        validateOwner(review, actorUserId);
        validateActiveForOwnerAction(review);

        review.update(rating, content.trim());
        return review;
    }

    @Transactional
    public void deleteReview(Long gearId, Long reviewId, Long actorUserId) {
        Review review = findReview(gearId, reviewId);
        validateOwner(review, actorUserId);
        validateActiveForOwnerAction(review);

        reviewRepository.delete(review);
    }

    @Transactional
    public Review deactivateReview(Long gearId, Long reviewId, String moderationReason, Long actorUserId) {
        Review review = findReview(gearId, reviewId);

        if (review.getStatus() == ReviewStatus.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 비활성화된 후기입니다.");
        }

        User moderator = userService.getActorUser(actorUserId);
        review.deactivate(moderationReason.trim(), moderator, LocalDateTime.now());
        return review;
    }

    @Transactional
    public Review activateReview(Long gearId, Long reviewId) {
        Review review = findReview(gearId, reviewId);

        if (review.getStatus() == ReviewStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 활성화된 후기입니다.");
        }

        review.activate();
        return review;
    }

    private Pageable pageRequest(int page, int size) {
        return PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    private Pageable adminPageRequest(int page, int size, String sort, String direction) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = "rating".equals(sort) ? "rating" : "createdAt";
        return PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(dir, sortField).and(Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    private Gear findGear(Long gearId) {
        return gearRepository.findById(gearId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장비를 찾을 수 없습니다."));
    }

    private Review findReview(Long gearId, Long reviewId) {
        return reviewRepository.findByIdAndGearId(reviewId, gearId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다."));
    }

    private void validateOwner(Review review, Long actorUserId) {
        if (actorUserId == null || !review.getUser().getId().equals(actorUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 후기만 수정하거나 삭제할 수 있습니다.");
        }
    }

    private void validateActiveForOwnerAction(Review review) {
        if (review.getStatus() != ReviewStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다.");
        }
    }
}
