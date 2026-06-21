package com.jam.ping.campsite.repository;

import com.jam.ping.campsite.domain.CampSite;
import com.jam.ping.regeion.district.code.DistrictCode;
import com.jam.ping.regeion.region.code.RegionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampSiteRepository extends JpaRepository<CampSite, Long> {

    Page<CampSite> findAllByOrderByIdDesc(Pageable pageable);

    Page<CampSite> findByNameContainingIgnoreCaseOrderByIdDesc(String keyword, Pageable pageable);

    Page<CampSite> findByRegionCodeOrderByIdDesc(RegionCode regionCode, Pageable pageable);

    Page<CampSite> findByRegionCodeAndNameContainingIgnoreCaseOrderByIdDesc(RegionCode regionCode, String keyword, Pageable pageable);

    Page<CampSite> findByDistrictCodeOrderByIdDesc(DistrictCode districtCode, Pageable pageable);

    Page<CampSite> findByDistrictCodeAndNameContainingIgnoreCaseOrderByIdDesc(DistrictCode districtCode, String keyword, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
