package com.jam.ping.maker.repository;

import com.jam.ping.maker.domain.Maker;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Maker, Long> {

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<Maker> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Page<Maker> findByNameContainingIgnoreCaseOrNameEngContainingIgnoreCase(
            String nameKeyword,
            String nameEngKeyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
    Optional<Maker> findWithUsersById(Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByNameEngIgnoreCase(String nameEng);

    boolean existsByNameEngIgnoreCaseAndIdNot(String nameEng, Long id);
}
