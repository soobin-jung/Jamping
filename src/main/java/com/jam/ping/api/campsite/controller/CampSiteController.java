package com.jam.ping.api.campsite.controller;

import com.jam.ping.api.campsite.controller.response.CampSitePageResponse;
import com.jam.ping.api.campsite.controller.response.CampSiteResponse;
import com.jam.ping.api.campsite.service.CampSiteService;
import com.jam.ping.global.request.KeywordPageRequest;
import com.jam.ping.global.response.ApiRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campsites")
public class CampSiteController {

    private final CampSiteService campSiteService;

    @GetMapping
    public ApiRes<CampSitePageResponse> getCampSites(@Valid @ModelAttribute KeywordPageRequest request) {
        return new ApiRes<CampSitePageResponse>()
                .successData(CampSitePageResponse.from(
                        campSiteService.getCampSites(request.keyword(), null, null, request.page(), request.size()),
                        request.keyword().trim(),
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
