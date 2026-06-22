package com.jam.ping.api.campingfam.main.service;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.main.repository.CampingFamRepository;
import com.jam.ping.api.campingfam.menu.service.CampingFamMenuService;
import com.jam.ping.api.campingfam.schedule.service.CampingFamScheduleTempService;
import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.campsite.repository.CampSiteRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampingFamService {

    private final CampingFamRepository campingFamRepository;
    private final CampingFamMenuService campingFamMenuService;
    private final CampingFamScheduleTempService campingFamScheduleTempService;
    private final CampSiteRepository campSiteRepository;

    public CampingFam getCampingFam(Long id) {
        return findCampingFam(id);
    }

    @Transactional
    public CampingFam createCampingFam(String name, Long campSiteId, String reservationSites) {
        CampSite campSite = findCampSite(campSiteId);
        CampingFam campingFam = CampingFam.builder()
                .name(name)
                .campSite(campSite)
                .reservationSites(reservationSites)
                .build();
        return campingFamRepository.save(campingFam);
    }

    @Transactional
    public CampingFam finalizeSchedule(Long campingFamId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "종료일은 시작일 이후여야 합니다.");
        }

        CampingFam campingFam = findCampingFam(campingFamId);
        campingFam.updateSchedule(startDate, endDate);
        campingFamMenuService.deleteMenusByCampingFamId(campingFamId);
        campingFamMenuService.initMenus(campingFam, startDate, endDate);

        return campingFam;
    }

    @Transactional
    public void deleteCampingFam(Long id) {
        CampingFam campingFam = findCampingFam(id);
        campingFamScheduleTempService.deleteAllByCampingFamId(id);
        campingFamMenuService.deleteMenusByCampingFamId(id);
        campingFamRepository.delete(campingFam);
    }

    private CampingFam findCampingFam(Long id) {
        return campingFamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "캠핑팸을 찾을 수 없습니다."));
    }

    private CampSite findCampSite(Long campSiteId) {
        return campSiteRepository.findById(campSiteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "캠핑장을 찾을 수 없습니다."));
    }
}
