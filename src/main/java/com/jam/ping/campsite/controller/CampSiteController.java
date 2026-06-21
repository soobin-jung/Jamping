package com.jam.ping.campsite.controller;

import com.jam.ping.campsite.controller.response.CampSitePageResponse;
import com.jam.ping.campsite.controller.response.CampSiteResponse;
import com.jam.ping.campsite.service.CampSiteService;
import com.jam.ping.global.response.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campsites")
public class CampSiteController {

    private final CampSiteService campSiteService;

    @GetMapping
    public ApiRes<CampSitePageResponse> getCampSites(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiRes<CampSitePageResponse>()
                .successData(CampSitePageResponse.from(
                        campSiteService.getCampSites(keyword, null, null, page, size),
                        keyword == null ? "" : keyword.trim(),
                        null,
                        null
                ));
    }

    @GetMapping("/{campSiteId}")
    public ApiRes<CampSiteResponse> getCampSite(@PathVariable Long campSiteId) {
        return new ApiRes<CampSiteResponse>()
                .successData(CampSiteResponse.from(campSiteService.getCampSite(campSiteId)));
    }
}
