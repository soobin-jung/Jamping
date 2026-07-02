package com.jam.ping.api.campingfam.main.service;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.main.dto.CampingFamDto;
import com.jam.ping.api.campingfam.main.repository.CampingFamRepository;
import com.jam.ping.api.campingfam.member.code.CampingFamRole;
import com.jam.ping.api.campingfam.member.domain.CampingFamMember;
import com.jam.ping.api.campingfam.member.repository.CampingFamMemberRepository;
import com.jam.ping.api.campingfam.menu.service.CampingFamMenuService;
import com.jam.ping.api.campingfam.schedule.service.CampingFamScheduleTempService;
import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.campsite.repository.CampSiteRepository;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.api.user.main.repository.UserRepository;
import com.jam.ping.global.exception.BadRequestException;
import com.jam.ping.global.exception.NotFoundException;
import com.jam.ping.global.security.AuthUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampingFamService {

    private final CampingFamRepository campingFamRepository;
    private final CampingFamMemberRepository campingFamMemberRepository;
    private final CampingFamMenuService campingFamMenuService;
    private final CampingFamScheduleTempService campingFamScheduleTempService;
    private final CampSiteRepository campSiteRepository;
    private final UserRepository userRepository;

    public CampingFamDto getCampingFam(Long id) {
        return CampingFamDto.from(findCampingFam(id));
    }

    @Transactional
    public CampingFamDto createCampingFam(String name, Long campSiteId, String reservationSites) {
        CampSite campSite = findCampSite(campSiteId);
        User creator = userRepository.findById(AuthUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        CampingFam campingFam = CampingFam.create(name, campSite, reservationSites);
        campingFamRepository.save(campingFam);

        campingFamMemberRepository.save(CampingFamMember.create(campingFam, creator, CampingFamRole.HOST));

        return CampingFamDto.from(campingFam);
    }

    @Transactional
    public CampingFamDto finalizeSchedule(Long campingFamId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("종료일은 시작일 이후여야 합니다.");
        }

        CampingFam campingFam = findCampingFam(campingFamId);
        campingFam.updateSchedule(startDate, endDate);
        campingFamMenuService.deleteMenusByCampingFamId(campingFamId);
        campingFamMenuService.initMenus(campingFam, startDate, endDate);

        return CampingFamDto.from(campingFam);
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
                .orElseThrow(() -> new NotFoundException("캠핑팸을 찾을 수 없습니다."));
    }

    private CampSite findCampSite(Long campSiteId) {
        return campSiteRepository.findById(campSiteId)
                .orElseThrow(() -> new NotFoundException("캠핑장을 찾을 수 없습니다."));
    }
}
