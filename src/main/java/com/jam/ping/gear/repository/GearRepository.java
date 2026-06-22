package com.jam.ping.gear.repository;

import com.jam.ping.gear.domain.Gear;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearRepository extends JpaRepository<Gear, Long> {

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByCategoryId(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByMakerId(Long makerId, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByCategoryIdAndMakerId(Long categoryId, Long makerId, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByMakerIdAndNameContainingIgnoreCase(Long makerId, String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "maker"})
    Page<Gear> findByCategoryIdAndMakerIdAndNameContainingIgnoreCase(
            Long categoryId,
            Long makerId,
            String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "maker"})
    Optional<Gear> findWithDetailsById(Long id);

    boolean existsByCategoryIdAndMakerIdAndNameIgnoreCase(Long categoryId, Long makerId, String name);

    boolean existsByCategoryIdAndMakerIdAndNameIgnoreCaseAndIdNot(
            Long categoryId,
            Long makerId,
            String name,
            Long id
    );
}
