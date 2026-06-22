package com.jam.ping.api.gear.maker.repository;

import com.jam.ping.api.gear.maker.domain.Maker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Maker, Long> {

    Page<Maker> findAll(Pageable pageable);

    Page<Maker> findByNameContainingIgnoreCaseOrNameEngContainingIgnoreCase(
            String nameKeyword,
            String nameEngKeyword,
            Pageable pageable
    );

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByNameEngIgnoreCase(String nameEng);

    boolean existsByNameEngIgnoreCaseAndIdNot(String nameEng, Long id);
}
