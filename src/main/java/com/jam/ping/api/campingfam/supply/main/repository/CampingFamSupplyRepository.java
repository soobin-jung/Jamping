package com.jam.ping.api.campingfam.supply.main.repository;

import com.jam.ping.api.campingfam.supply.main.domain.CampingFamSupply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamSupplyRepository extends JpaRepository<CampingFamSupply, Long> {

    List<CampingFamSupply> findByCampingFamId(Long campingFamId);

    void deleteByCampingFamId(Long campingFamId);
}
