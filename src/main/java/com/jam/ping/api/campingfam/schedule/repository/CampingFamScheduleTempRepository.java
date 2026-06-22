package com.jam.ping.api.campingfam.schedule.repository;

import com.jam.ping.api.campingfam.schedule.domain.CampingFamScheduleTemp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamScheduleTempRepository extends JpaRepository<CampingFamScheduleTemp, Long> {

    List<CampingFamScheduleTemp> findByCampingFamId(Long campingFamId);

    void deleteByCampingFamIdAndUserId(Long campingFamId, Long userId);

    void deleteAllByCampingFamId(Long campingFamId);
}
