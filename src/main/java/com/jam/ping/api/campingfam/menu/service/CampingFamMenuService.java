package com.jam.ping.api.campingfam.menu.service;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import com.jam.ping.api.campingfam.menu.repository.CampingFamMenuRepository;
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
public class CampingFamMenuService {

    private final CampingFamMenuRepository campingFamMenuRepository;

    @Transactional
    public void initMenus(CampingFam campingFam, LocalDate startDate, LocalDate endDate) {
        List<CampingFamMenu> menus = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> CampingFamMenu.builder()
                        .campingFam(campingFam)
                        .campingDate(date)
                        .build())
                .toList();
        campingFamMenuRepository.saveAll(menus);
    }

    public List<CampingFamMenu> getMenus(Long campingFamId) {
        return campingFamMenuRepository.findByCampingFamIdOrderByCampingDateAsc(campingFamId);
    }

    @Transactional
    public CampingFamMenu updateMenu(Long campingFamId, Long menuId, String breakfast, String lunch, String dinner, String snack) {
        CampingFamMenu menu = campingFamMenuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        if (!menu.getCampingFam().getId().equals(campingFamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 캠핑팸의 메뉴가 아닙니다.");
        }

        menu.update(breakfast, lunch, dinner, snack);
        return menu;
    }

    @Transactional
    public void deleteMenusByCampingFamId(Long campingFamId) {
        campingFamMenuRepository.deleteByCampingFamId(campingFamId);
    }
}
