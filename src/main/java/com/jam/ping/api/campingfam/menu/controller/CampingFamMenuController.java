package com.jam.ping.api.campingfam.menu.controller;

import com.jam.ping.api.campingfam.menu.controller.request.CampingFamMenuUpdateRequest;
import com.jam.ping.api.campingfam.menu.controller.response.CampingFamMenuResponse;
import com.jam.ping.api.campingfam.menu.domain.CampingFamMenu;
import com.jam.ping.api.campingfam.menu.service.CampingFamMenuService;
import com.jam.ping.global.response.ApiRes;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camping-fams/{campingFamId}/menus")
public class CampingFamMenuController {

    private final CampingFamMenuService campingFamMenuService;

    @GetMapping
    public ApiRes<List<CampingFamMenuResponse>> getMenus(@PathVariable Long campingFamId) {
        List<CampingFamMenuResponse> responses = campingFamMenuService.getMenus(campingFamId)
                .stream()
                .map(CampingFamMenuResponse::from)
                .toList();
        return new ApiRes<List<CampingFamMenuResponse>>().successData(responses);
    }

    @PutMapping("/{menuId}")
    public ApiRes<CampingFamMenuResponse> updateMenu(
            @PathVariable Long campingFamId,
            @PathVariable Long menuId,
            @Valid @RequestBody CampingFamMenuUpdateRequest request
    ) {
        CampingFamMenu menu = campingFamMenuService.updateMenu(
                campingFamId,
                menuId,
                request.breakfast(),
                request.lunch(),
                request.dinner(),
                request.snack()
        );
        return new ApiRes<CampingFamMenuResponse>()
                .successData(CampingFamMenuResponse.from(menu))
                .manipulationOne(menu.getId())
                .responseMsg("식단이 수정되었습니다.");
    }
}
