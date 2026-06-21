package com.jam.ping.campsite.controller;

import com.jam.ping.campsite.controller.request.CampSiteRequest;
import com.jam.ping.campsite.controller.response.CampSitePageResponse;
import com.jam.ping.campsite.controller.response.CampSiteResponse;
import com.jam.ping.campsite.domain.CampSite;
import com.jam.ping.campsite.service.CampSiteService;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.global.security.AdminOnly;
import com.jam.ping.regeion.district.code.DistrictCode;
import com.jam.ping.regeion.region.code.RegionCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminOnly
@RequiredArgsConstructor
@RequestMapping("/admin/campsites")
public class CampSiteAdminController {

    private final CampSiteService campSiteService;

    @GetMapping
    public ApiRes<CampSitePageResponse> getCampSites(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) RegionCode regionCode,
            @RequestParam(required = false) DistrictCode districtCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<CampSitePageResponse>()
                .successData(CampSitePageResponse.from(
                        campSiteService.getCampSites(keyword, regionCode, districtCode, page, size),
                        keyword == null ? "" : keyword.trim(),
                        regionCode,
                        districtCode
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
