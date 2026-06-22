package com.jam.ping.api.gear.review.repository;

import com.jam.ping.api.gear.review.code.ReviewStatus;
import com.jam.ping.api.gear.review.domain.Review;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"gear", "user", "moderatedBy"})
    Page<Review> findByGearIdAndStatus(Long gearId, ReviewStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"gear", "user", "moderatedBy"})
    Page<Review> findByGearId(Long gearId, Pageable pageable);

    @EntityGraph(attributePaths = {"gear", "user", "moderatedBy"})
    Optional<Review> findByIdAndGearId(Long reviewId, Long gearId);

    Optional<Review> findByGearIdAndUserId(Long gearId, Long userId);

    boolean existsByGearIdAndUserId(Long gearId, Long userId);
}
