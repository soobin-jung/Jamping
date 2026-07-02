package com.jam.ping.api.campingfam.schedule.service;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.main.repository.CampingFamRepository;
import com.jam.ping.api.campingfam.schedule.domain.CampingFamScheduleTemp;
import com.jam.ping.api.campingfam.schedule.dto.CampingFamScheduleTempDto;
import com.jam.ping.api.campingfam.schedule.repository.CampingFamScheduleTempRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampingFamScheduleTempService {

    private final CampingFamScheduleTempRepository campingFamScheduleTempRepository;
    private final CampingFamRepository campingFamRepository;

    @Transactional
    public List<CampingFamScheduleTempDto> submitDates(Long campingFamId, Long userId, List<LocalDate> dates) {
        CampingFam campingFam = findCampingFam(campingFamId);
        campingFamScheduleTempRepository.deleteByCampingFamIdAndUserId(campingFamId, userId);

        List<CampingFamScheduleTemp> temps = dates.stream()
                .map(date -> CampingFamScheduleTemp.create(campingFam, userId, date))
                .toList();

        return campingFamScheduleTempRepository.saveAll(temps).stream()
                .map(CampingFamScheduleTempDto::from)
                .toList();
    }

    public List<CampingFamScheduleTempDto> getTempDates(Long campingFamId) {
        return campingFamScheduleTempRepository.findByCampingFamId(campingFamId).stream()
                .map(CampingFamScheduleTempDto::from)
                .toList();
    }

    @Transactional
    public void deleteMyDates(Long campingFamId, Long userId) {
        campingFamScheduleTempRepository.deleteByCampingFamIdAndUserId(campingFamId, userId);
    }

    @Transactional
    public void deleteAllByCampingFamId(Long campingFamId) {
        campingFamScheduleTempRepository.deleteAllByCampingFamId(campingFamId);
    }

    private CampingFam findCampingFam(Long campingFamId) {
        return campingFamRepository.findById(campingFamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "캠핑팸을 찾을 수 없습니다."));
    }
}
