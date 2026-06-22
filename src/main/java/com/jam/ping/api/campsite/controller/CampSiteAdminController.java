package com.jam.ping.api.campsite.controller;

import com.jam.ping.api.campsite.controller.request.CampSiteRequest;
import com.jam.ping.api.campsite.controller.request.CampSiteSearchRequest;
import com.jam.ping.api.campsite.controller.response.CampSitePageResponse;
import com.jam.ping.api.campsite.controller.response.CampSiteResponse;
import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.campsite.service.CampSiteService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/campsites")
public class CampSiteAdminController {

    private final CampSiteService campSiteService;

    @GetMapping
    public ApiRes<CampSitePageResponse> getCampSites(@Valid @ModelAttribute CampSiteSearchRequest request) {
        return new ApiRes<CampSitePageResponse>()
                .successData(CampSitePageResponse.from(
                        campSiteService.getCampSites(request.keyword(), request.regionCode(), request.districtCode(), request.page(), request.size()),
                        request.keyword().trim(),
                        request.regionCode(),
                        request.districtCode()
                ));
    }

    @GetMapping("/{campSiteId}")
    public ApiRes<CampSiteResponse> getCampSite(@PathVariable Long campSiteId) {
        return new ApiRes<CampSiteResponse>()
                .successData(CampSiteResponse.from(campSiteService.getCampSite(campSiteId)));
    }

    @PostMapping
    public ResponseEntity<ApiRes<CampSiteResponse>> createCampSite(@Valid @RequestBody CampSiteRequest request) {
        CampSite campSite = campSiteService.createCampSite(
                request.name(),
                request.link(),
                request.regionCode(),
                request.districtCode(),
                request.checkInTime(),
                request.checkOutTime()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<CampSiteResponse>()
                        .successData(CampSiteResponse.from(campSite))
                        .manipulationOne(campSite.getId())
                        .responseMsg("캠핑장 정보가 등록되었습니다."));
    }

    @PutMapping("/{campSiteId}")
    public ApiRes<CampSiteResponse> updateCampSite(
            @PathVariable Long campSiteId,
            @Valid @RequestBody CampSiteRequest request
    ) {
        CampSite campSite = campSiteService.updateCampSite(
                campSiteId,
                request.name(),
                request.link(),
                request.regionCode(),
                request.districtCode(),
                request.checkInTime(),
                request.checkOutTime()
        );

        return new ApiRes<CampSiteResponse>()
                .successData(CampSiteResponse.from(campSite))
                .manipulationOne(campSite.getId())
                .responseMsg("캠핑장 정보가 수정되었습니다.");
    }

    @DeleteMapping("/{campSiteId}")
    public ApiRes<Void> deleteCampSite(@PathVariable Long campSiteId) {
        campSiteService.deleteCampSite(campSiteId);

        return new ApiRes<Void>()
                .manipulationOne(campSiteId)
                .responseMsg("캠핑장 정보가 삭제되었습니다.");
    }
}
