package com.jam.ping.api.campingfam.menu.repository;

import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamMenuRepository extends JpaRepository<CampingFamMenu, Long> {

    List<CampingFamMenu> findByCampingFamIdOrderByCampingDateAsc(Long campingFamId);

    void deleteByCampingFamId(Long campingFamId);
}
