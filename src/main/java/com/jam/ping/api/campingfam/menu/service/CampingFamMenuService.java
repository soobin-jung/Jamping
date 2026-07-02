package com.jam.ping.api.campingfam.menu.service;

import com.jam.ping.api.campingfam.main.domain.CampingFam;
import com.jam.ping.api.campingfam.menu.code.MealType;
import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import com.jam.ping.api.campingfam.menu.dto.CampingFamMenuDto;
import com.jam.ping.api.campingfam.menu.repository.CampingFamMenuRepository;
import java.time.LocalDate;
import java.util.Arrays;
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
                .flatMap(date -> Arrays.stream(MealType.values())
                    .map(mealType -> CampingFamMenu.create(campingFam, date, mealType)))
                .toList();
        campingFamMenuRepository.saveAll(menus);
    }

    public List<CampingFamMenuDto> getMenus(Long campingFamId) {
        return campingFamMenuRepository.findByCampingFamIdOrderByCampingDateAsc(campingFamId).stream()
                .map(CampingFamMenuDto::from)
                .toList();
    }

    @Transactional
    public CampingFamMenuDto updateMenu(Long campingFamId, Long menuId, String menu) {
        CampingFamMenu campingFamMenu = campingFamMenuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        if (!campingFamMenu.getCampingFam().getId().equals(campingFamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 캠핑팸의 메뉴가 아닙니다.");
        }

        campingFamMenu.update(menu);
        return CampingFamMenuDto.from(campingFamMenu);
    }

    @Transactional
    public void deleteMenusByCampingFamId(Long campingFamId) {
        campingFamMenuRepository.deleteByCampingFamId(campingFamId);
    }
}
