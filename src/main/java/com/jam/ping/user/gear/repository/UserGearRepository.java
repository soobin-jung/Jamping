package com.jam.ping.user.gear.repository;

import com.jam.ping.user.gear.domain.UserGear;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGearRepository extends JpaRepository<UserGear, Long> {

    @EntityGraph(attributePaths = {"user", "category", "maker", "gear"})
    Page<UserGear> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "category", "maker", "gear"})
    Page<UserGear> findAllByOrderByIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "category", "maker", "gear"})
    Optional<UserGear> findById(Long id);
}
